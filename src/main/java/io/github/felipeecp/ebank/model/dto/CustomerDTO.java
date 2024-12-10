package io.github.felipeecp.ebank.model.dto;

import jakarta.validation.constraints.*;

public record CustomerDTO(
        Long id,
        @NotBlank(message = "Nome é necessário")
        @Size(min = 3, message = "O noe deve ter mais que 3 letras")
        String name,
        @NotNull(message = "É necessário informar a idade")
        Integer age,
        @NotBlank(message = "Email é obrigatorio")
        @Email(message = "Email invalido")
        String email,
        String accountNumber
) {

}
