package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.AuthRequestDTO;
import io.github.felipeecp.ebank.model.dto.AuthResponseDTO;
import io.github.felipeecp.ebank.model.dto.RegisterRequestDTO;
import io.github.felipeecp.ebank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Tag(name = "Autenticação", description = "API para registro e autenticação de usuários")
@SecurityRequirements  // Indica que estes endpoints não requerem autenticação
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(
            summary = "Registrar novo usuário",
            description = "Cria um novo registro de usuário no sistema"
    )
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @Parameter(
                    description = "Dados de registro do usuário",
                    required = true
            )
            @Valid @RequestBody RegisterRequestDTO request
    ) throws BusinessException {
        authService.register(request);
    }

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário e retorna o token de acesso"
    )
    @PostMapping("/login")
    public AuthResponseDTO login(
            @Parameter(
                    description = "Credenciais do usuário",
                    required = true
            )
            @Valid @RequestBody AuthRequestDTO request
    ) throws BusinessException {
        return authService.login(request);
    }
}