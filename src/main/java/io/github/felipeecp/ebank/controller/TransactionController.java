package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.*;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.service.AccountService;
import io.github.felipeecp.ebank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
@Tag(name = "Transações", description = "API para gerenciamento de transações bancárias")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @Operation(
            summary = "Processar transação",
            description = "Processa uma nova transação bancária"
    )
    @PostMapping
    public void processTransaction(
            @Parameter(description = "Dados da transação a ser processada", required = true)
            @Valid @RequestBody TransactionDTO transactionDTO
    ) throws BusinessException {
        transactionService.processTransaction(transactionDTO);
    }

    @Operation(
            summary = "Consultar saldo",
            description = "Recupera o saldo atual de uma conta"
    )
    @GetMapping("/balance/{accountNumber}")
    public AccountBalanceDTO getBalance(
            @Parameter(description = "Número da conta", required = true, example = "12345-6")
            @PathVariable String accountNumber
    ) throws BusinessException {
        return transactionService.getBalance(accountNumber);
    }

    @Operation(
            summary = "Listar transações",
            description = "Recupera o histórico de transações de uma conta"
    )
    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransaction(
            @Parameter(description = "Número da conta", required = true, example = "12345-6")
            @PathVariable String accountNumber
    ) throws BusinessException {
        return transactionService.getTransactions(accountNumber);
    }

    @Operation(
            summary = "Realizar depósito",
            description = "Processa um depósito em uma conta"
    )
    @PostMapping("/deposit")
    public DepositResponseDTO deposit(
            @Parameter(description = "Dados do depósito", required = true)
            @Valid @RequestBody DepositRequestDTO request
    ) throws BusinessException {
        return accountService.deposit(request);
    }

    @Operation(
            summary = "Realizar transferência",
            description = "Processa uma transferência entre contas"
    )
    @PostMapping("/transfer")
    public TransferResponseDTO transfer(
            @Parameter(description = "Dados da transferência", required = true)
            @Valid @RequestBody TransferRequestDTO request
    ) throws BusinessException {
        return accountService.transfer(request);
    }
}