package ru.alexey.transfers.ledger.entity;

import jakarta.persistence.*;
import ru.alexey.transfers.ledger.entity.enums.HoldStatusEnum;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "holds")
public class HoldEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "operation_id")
  private OperationEntity operation;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_id")
  private AccountEntity account;

  private BigDecimal amount;

  @Enumerated(EnumType.STRING)
  private HoldStatusEnum status;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  public HoldEntity() {
  }

  public HoldEntity(OperationEntity operation, AccountEntity account, BigDecimal amount, HoldStatusEnum status) {
    this.operation = operation;
    this.account = account;
    this.amount = amount;
    this.status = status;
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

  public HoldStatusEnum getStatus() {
    return status;
  }

  public void setStatus(HoldStatusEnum status) {
    this.status = status;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}