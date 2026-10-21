package ru.alexey.transfers.ledger.entity;

import jakarta.persistence.*;
import ru.alexey.transfers.ledger.entity.enums.OperationResultEnum;
import ru.alexey.transfers.ledger.entity.enums.OperationTypeEnum;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "operations")
public class OperationEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "reference_id")
  private UUID referenceId;

  @Enumerated(EnumType.STRING)
  private OperationTypeEnum type;

  @Enumerated(EnumType.STRING)
  private OperationResultEnum result;

  @Column(name = "created_at", insertable = false, updatable = false)
  private Instant createdAt;

  public OperationEntity() {
  }

  public OperationEntity(UUID referenceId, OperationTypeEnum type, OperationResultEnum result) {
    this.referenceId = referenceId;
    this.type = type;
    this.result = result;
  }

  public Long getId() {
    return id;
  }

  public UUID getReferenceId() {
    return referenceId;
  }

  public OperationTypeEnum getType() {
    return type;
  }

  public OperationResultEnum getResult() {
    return result;
  }

  public void setResult(OperationResultEnum result) {
    this.result = result;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
