package io.github.felipeecp.ebank.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreditLimitRequestDTO(
        @NotBlank(message = "Número da conta é obrigatório")
        String accountNumber,

        @NotNull(message = "Novo limite é obrigatório")
        @Positive(message = "Limite deve ser positivo")
        BigDecimal newCreditLimit
) {}
