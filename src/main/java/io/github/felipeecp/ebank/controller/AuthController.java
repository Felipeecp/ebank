package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.AuthRequestDTO;
import io.github.felipeecp.ebank.model.dto.AuthResponseDTO;
import io.github.felipeecp.ebank.model.dto.RegisterRequestDTO;
import io.github.felipeecp.ebank.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterRequestDTO request) throws BusinessException {
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponseDTO login(@Valid @RequestBody AuthRequestDTO request) throws BusinessException {
        return authService.login(request);
    }
}
