package io.github.felipeecp.ebank.model.dto;

import io.github.felipeecp.ebank.model.enums.TransactionType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransactionDTO(
        @NotNull(message = "Número da conta é obrigatório")
        String accountNumber,
        @NotNull(message = "Tipo de transação é obrigatório")
        TransactionType type,
        @NotNull(message = "Necessário informar um valor")
        @Positive(message = "A quantia deve ser positiva")
        BigDecimal amount
) {
}
