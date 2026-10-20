--liquibase formatted sql

--changeset alexey:001-create-accounts
CREATE TABLE accounts
(
    id         bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    type       varchar        NOT NULL CHECK (type IN ('CLIENT', 'SYSTEM')),
    client_id  varchar(64),
    code       varchar UNIQUE,
    currency   varchar(3)     NOT NULL CHECK (currency IN ('RUB')),
    balance    numeric(19, 2) NOT NULL DEFAULT 0,
    reserved   numeric(19, 2) NOT NULL DEFAULT 0,
    status     varchar        NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'BLOCKED', 'CLOSED')),
    created_at timestamptz    NOT NULL DEFAULT now(),

    -- у клиентского счёта есть владелец, у системного нет
    CONSTRAINT chk_accounts_client_id CHECK ((type = 'CLIENT') = (client_id IS NOT NULL)),
    -- у системного счёта есть имя, у клиентского нет
    CONSTRAINT chk_accounts_code CHECK ((type = 'SYSTEM') = (code IS NOT NULL)),
    -- клиентский счёт не уходит в минус; системному можно
    CONSTRAINT chk_accounts_balance CHECK (type = 'SYSTEM' OR balance >= 0),
    -- холд не отрицательный и не больше баланса; у системных счетов холдов нет
    CONSTRAINT chk_accounts_reserved CHECK (
        (type = 'CLIENT' AND reserved >= 0 AND reserved <= balance)
            OR (type = 'SYSTEM' AND reserved = 0)
        )
);
CREATE INDEX idx_accounts_client_id ON accounts (client_id);

--changeset alexey:002-create-operations
CREATE TABLE operations
(
    id           bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    reference_id uuid        NOT NULL,
    type         varchar     NOT NULL CHECK (type IN
                                             ('DEPOSIT', 'TRANSFER', 'RESERVE', 'COMMIT_RESERVE', 'RELEASE_RESERVE')),
    result       varchar     NOT NULL CHECK (result IN
                                             ('OK', 'INSUFFICIENT_FUNDS', 'ACCOUNT_NOT_FOUND', 'ACCOUNT_NOT_ACTIVE',
                                              'HOLD_NOT_FOUND', 'HOLD_NOT_ACTIVE')),
    created_at   timestamptz NOT NULL DEFAULT now(),

    -- ключ идемпотентности: одна операция каждого типа на один документ-основание;
    -- индекс этого ограничения покрывает и поиск всех операций по reference_id
    CONSTRAINT uq_operations_reference_type UNIQUE (reference_id, type)
);

--changeset alexey:003-create-entries
CREATE TABLE entries
(
    id           bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- операция, создавшая проводку. Проводки создают только успешные DEPOSIT, TRANSFER
    -- и COMMIT_RESERVE (по две на операцию); сумма проводок одной операции = 0
    operation_id bigint         NOT NULL REFERENCES operations (id),
    account_id   bigint         NOT NULL REFERENCES accounts (id),
    -- минус — списание, плюс — зачисление
    amount       numeric(19, 2) NOT NULL CHECK (amount <> 0),
    created_at   timestamptz    NOT NULL DEFAULT now()
);
-- PostgreSQL не создаёт индексы под внешние ключи автоматически
-- выписка по счёту; сверка balance = сумма проводок счёта
CREATE INDEX idx_entries_account_id ON entries (account_id);
-- проводки операции; сверка «сумма проводок операции = 0»
CREATE INDEX idx_entries_operation_id ON entries (operation_id);

--changeset alexey:004-create-holds
CREATE TABLE holds
(
    id           bigint GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    -- операция RESERVE, которая поставила холд. Уникальна: одна RESERVE на перевод
    -- (см. uq_operations_reference_type) → один холд на перевод.
    -- CommitReserve и ReleaseReserve находят холд через операцию (reference_id, 'RESERVE')
    operation_id bigint         NOT NULL UNIQUE REFERENCES operations (id),
    account_id   bigint         NOT NULL REFERENCES accounts (id),
    amount       numeric(19, 2) NOT NULL CHECK (amount > 0),
    status       varchar        NOT NULL DEFAULT 'ACTIVE' CHECK (status IN ('ACTIVE', 'COMMITTED', 'RELEASED')),
    created_at   timestamptz    NOT NULL DEFAULT now()
);
CREATE INDEX idx_holds_account_id ON holds (account_id);
-- сверка reserved = сумма активных холдов

--changeset alexey:005-insert-system-accounts
INSERT INTO accounts (type, code, currency)
VALUES ('SYSTEM', 'EXTERNAL_FUNDING', 'RUB'), -- источник пополнений, может уходить в минус
       ('SYSTEM', 'INTERBANK_SETTLEMENT', 'RUB'); -- корреспондентский счёт для переводов в другие банки