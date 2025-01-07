package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.*;
import io.github.felipeecp.ebank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Tag(name = "Contas", description = "API para gerenciamento de contas bancárias")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @Operation(
            summary = "Ajustar limite de crédito",
            description = "Permite alterar o limite de crédito de uma conta específica"
    )
    @PutMapping("/credit-limit")
    public CreditLimitResponseDTO adjustCreditLimit(
            @Parameter(description = "Dados para ajuste do limite de crédito")
            @Valid @RequestBody CreditLimitRequestDTO request
    ) throws BusinessException {
        return accountService.adjustCreditLimit(request);
    }

    @Operation(
            summary = "Listar contas disponíveis",
            description = "Retorna uma lista com todas as contas disponíveis no sistema"
    )
    @GetMapping("/available")
    public List<AccountSummaryDTO> getAvailableAccounts() {
        return accountService.getAvailableAccounts();
    }

    @Operation(
            summary = "Consultar limites da conta",
            description = "Recupera informações sobre os limites disponíveis de uma conta específica"
    )
    @GetMapping("/{accountNumber}/limits")
    public AccountLimitsDTO getAccountsLimits(
            @Parameter(description = "Número da conta a ser consultada")
            @PathVariable String accountNumber
    ) throws BusinessException {
        return accountService.getAcountLimits(accountNumber);
    }

    @Operation(
            summary = "Consultar extrato da conta",
            description = "Recupera o extrato de uma conta com opção de filtro por período"
    )
    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<AccountStatementDTO> getStatement(
            @Parameter(description = "Número da conta para geração do extrato")
            @PathVariable String accountNumber,

            @Parameter(description = "Data inicial do período (opcional, formato ISO)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,

            @Parameter(description = "Data final do período (opcional, formato ISO)")
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate
    ) throws BusinessException {
        return ResponseEntity.ok(accountService.getAccountStatement(accountNumber, startDate, endDate));
    }
}