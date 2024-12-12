package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record CreditLimitResponseDTO(
        String accountNumber,
        BigDecimal creditLimit,
        BigDecimal availableCredit
) {
}
