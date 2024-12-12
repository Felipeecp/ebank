package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record AccountTransferInfo(
        String accountNumber,
        BigDecimal remainingBalance,
        BigDecimal remainingCredit
) {
}
