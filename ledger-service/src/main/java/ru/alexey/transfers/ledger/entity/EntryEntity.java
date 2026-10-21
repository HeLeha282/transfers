package ru.alexey.transfers.ledger.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "entries")
@Immutable
public class EntryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "operation_id")
  private OperationEntity operation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private AccountEntity account;

  private BigDecimal amount;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  public EntryEntity() {
  }

  public EntryEntity(OperationEntity operation, AccountEntity account, BigDecimal amount) {
    this.operation = operation;
    this.account = account;
    this.amount = amount;
  }

  public Long getId() {
    return id;
  }

  public OperationEntity getOperation() {
    return operation;
  }

  public AccountEntity getAccount() {
    return account;
  }

  public BigDecimal getAmount() {
    return amount;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}