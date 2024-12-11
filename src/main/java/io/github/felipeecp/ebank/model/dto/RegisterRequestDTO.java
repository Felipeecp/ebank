package io.github.felipeecp.ebank.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterRequestDTO(
        @NotBlank(message = "Email é obrigatório")
        @Email(message = "O email esta com formato invalido")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        @Size(min = 6, message = "A senha deve ter ao menos 6 caracteres")
        String password,
        @NotBlank(message = "Nome é necessário")
        @Size(min = 3, message = "O noe deve ter mais que 3 letras")
        String name,
        @NotNull(message = "É necessário informar a idade")
        Integer age
) {
}
