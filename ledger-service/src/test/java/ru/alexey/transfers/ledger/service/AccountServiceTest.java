package ru.alexey.transfers.ledger.service;

import java.math.BigDecimal;
import java.util.NoSuchElementException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import ru.alexey.transfers.ledger.TestcontainersConfiguration;
import ru.alexey.transfers.ledger.dto.AccountResponseDTO;
import ru.alexey.transfers.ledger.entity.enums.AccountCurrencyEnum;
import ru.alexey.transfers.ledger.entity.enums.AccountStatusEnum;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
class AccountServiceTest {

  @Autowired
  private AccountService accountService;

  @Test
  void createAccount_newClient_activeWithZeroBalance() {
    // given — подготовить данные
    long id = accountService.createAccount("Вася", AccountCurrencyEnum.RUB);

    // when — вызвать проверяемый метод
    AccountResponseDTO gotAccount = accountService.getAccount(id);

    // then — проверить результат
    assertThat(gotAccount.id()).isEqualTo(id);
    assertThat(gotAccount.balance()).isEqualByComparingTo(new BigDecimal("0"));
    assertThat(gotAccount.available()).isEqualByComparingTo(new BigDecimal("0.00"));
    assertThat(gotAccount.status()).isEqualTo(AccountStatusEnum.ACTIVE);
  }

  @Test
  void getAccount_unknownId_throws() {
    assertThatThrownBy(() -> accountService.getAccount(Long.MAX_VALUE))
        .isInstanceOf(NoSuchElementException.class);
  }
}
