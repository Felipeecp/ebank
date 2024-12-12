package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record AccountLimitsDTO(
        String accountNumber,
        BigDecimal balance,
        BigDecimal creditLimit,
        BigDecimal availableCredit,
        BigDecimal totalAvailable
) {
}
