package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransactionHistoryDTO(
        String type,
        BigDecimal amount,
        LocalDateTime dateTime,
        String description,
        String relatedAccountNumber
) {}
