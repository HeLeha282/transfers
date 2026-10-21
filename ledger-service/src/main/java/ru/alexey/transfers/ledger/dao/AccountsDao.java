package ru.alexey.transfers.ledger.dao;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.stereotype.Repository;
import ru.alexey.transfers.ledger.entity.AccountEntity;

@Repository
public class AccountsDao {
  private final EntityManager entityManager;

  public AccountsDao(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void persist(AccountEntity entity) {
    entityManager.persist(entity);
  }

  public Optional<AccountEntity> findById(Long id) {
    return Optional.ofNullable(entityManager.find(AccountEntity.class, id));
  }
}
