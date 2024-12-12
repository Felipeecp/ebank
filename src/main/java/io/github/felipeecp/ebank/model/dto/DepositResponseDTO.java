package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record DepositResponseDTO(
        String message,
        BigDecimal newBalance
) {
}
