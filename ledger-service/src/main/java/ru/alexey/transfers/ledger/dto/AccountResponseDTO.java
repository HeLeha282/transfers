package ru.alexey.transfers.ledger.dto;

import java.math.BigDecimal;
import ru.alexey.transfers.ledger.entity.enums.AccountStatusEnum;

public record AccountResponseDTO (Long id, BigDecimal balance, BigDecimal available, AccountStatusEnum status) {

}
