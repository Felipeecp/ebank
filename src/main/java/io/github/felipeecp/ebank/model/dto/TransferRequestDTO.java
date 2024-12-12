package io.github.felipeecp.ebank.model.dto;

import io.github.felipeecp.ebank.model.enums.TransferType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequestDTO(
        @NotBlank(message = "Conta de origem é obrigatória")
        String fromAccountNumber,

        @NotBlank(message = "Conta de destino é obrigatória")
        String toAccountNumber,

        @NotNull(message = "Valor é obrigatório")
        @Positive(message = "Valor deve ser positivo")
        BigDecimal amount,

        @NotNull(message = "Tipo de transferência é obrigatório")
        TransferType type
) {

}
