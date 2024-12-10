package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

record AccountBalanceDTO(
        String accountNumber,
        BigDecimal balance,
        String customerName
) {
}
