package io.github.felipeecp.ebank.model.dto;

public record AuthResponseDTO(
        String token,
        String email,
        String name,
        String accountNumber
) {
}
