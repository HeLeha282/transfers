package ru.alexey.transfers.ledger.entity;


import jakarta.persistence.*;
import ru.alexey.transfers.ledger.entity.enums.AccountCodeEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountCurrencyEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountStatusEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountTypeEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "accounts")
public class AccountEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Enumerated(EnumType.STRING)
  private AccountTypeEnum type;

  @Column(name = "client_id")
  private String clientId;

  @Enumerated(EnumType.STRING)
  private AccountCodeEnum code;

  @Enumerated(EnumType.STRING)
  private AccountCurrencyEnum currency;

  private BigDecimal balance;

  private BigDecimal reserved;

  @Enumerated(EnumType.STRING)
  private AccountStatusEnum status;
  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  public AccountEntity() {
  }

  public AccountEntity(
      AccountTypeEnum type,
      String clientId,
      AccountCodeEnum code,
      AccountCurrencyEnum currency,
      BigDecimal balance,
      BigDecimal reserved,
      AccountStatusEnum status
  ) {
    this.type = type;
    this.clientId = clientId;
    this.code = code;
    this.currency = currency;
    this.balance = balance;
    this.reserved = reserved;
    this.status = status;
  }

  public Long getId() {
    return id;
  }

  public AccountTypeEnum getType() {
    return type;
  }

  public String getClientId() {
    return clientId;
  }

  public AccountCodeEnum getCode() {
    return code;
  }

  public AccountCurrencyEnum getCurrency() {
    return currency;
  }

  public BigDecimal getBalance() {
    return balance;
  }

  public void setBalance(BigDecimal balance) {
    this.balance = balance;
  }

  public BigDecimal getReserved() {
    return reserved;
  }

  public void setReserved(BigDecimal reserved) {
    this.reserved = reserved;
  }

  public AccountStatusEnum getStatus() {
    return status;
  }

  public void setStatus(AccountStatusEnum status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
