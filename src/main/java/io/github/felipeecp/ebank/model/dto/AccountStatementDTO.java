package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;
import java.util.List;

public record AccountStatementDTO(
        String accountNumber,
        String customerName,
        BigDecimal currentBalance,
        List<TransactionHistoryDTO> transactions
) {
}
