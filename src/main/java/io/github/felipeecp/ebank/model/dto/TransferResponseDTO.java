package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record TransferResponseDTO(
        String transactionId,
        String dateTime,
        AccountTransferInfo fromAccount,
        AccountTransferInfo toAccount,
        BigDecimal amount
) {
}
