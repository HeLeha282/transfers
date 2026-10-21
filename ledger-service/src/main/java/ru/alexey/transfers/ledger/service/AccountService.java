package ru.alexey.transfers.ledger.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alexey.transfers.ledger.dao.AccountsDao;
import ru.alexey.transfers.ledger.dto.AccountResponseDTO;
import ru.alexey.transfers.ledger.entity.AccountEntity;
import ru.alexey.transfers.ledger.entity.enums.AccountCurrencyEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountStatusEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountTypeEnum;

@Service
public class AccountService {
  private final AccountsDao accountsDao;

  public AccountService(AccountsDao accountsDao) {
    this.accountsDao = accountsDao;
  }

  @Transactional
  public long createAccount(String clientId, AccountCurrencyEnum currency) {
    AccountEntity entity = new AccountEntity(
        AccountTypeEnum.CLIENT, clientId, null, currency, new BigDecimal("0.00"), new BigDecimal("0.00"), AccountStatusEnum.ACTIVE
    );
    accountsDao.persist(entity);
    return entity.getId();
  }

  @Transactional(readOnly = true)
  public AccountResponseDTO getAccount(Long id) {
    AccountEntity account = accountsDao.findById(id).orElseThrow(NoSuchElementException::new); // надо сделать глобальный обработчик ошибок и свое исключение
    BigDecimal balance = account.getBalance();
    BigDecimal reserved = account.getReserved();
    return new AccountResponseDTO(account.getId(), balance, balance.subtract(reserved), account.getStatus());
  }
}
