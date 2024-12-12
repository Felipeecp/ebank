package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.*;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.service.AccountService;
import io.github.felipeecp.ebank.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;
    private final AccountService accountService;

    public TransactionController(TransactionService transactionService, AccountService accountService) {
        this.transactionService = transactionService;
        this.accountService = accountService;
    }

    @PostMapping
    public void processTransaction(@Valid @RequestBody TransactionDTO transactionDTO) throws BusinessException {
        transactionService.processTransaction(transactionDTO);
    }

    @GetMapping("/balance/{accountNumber}")
    public AccountBalanceDTO getBalance(@PathVariable String accountNumber) throws BusinessException {
        return transactionService.getBalance(accountNumber);
    }

    @GetMapping("/{accountNumber}")
    public List<Transaction> getTransaction(@PathVariable String accountNumber) throws BusinessException {
        return transactionService.getTransactions(accountNumber);
    }

    @PostMapping("/deposit")
    public DepositResponseDTO deposit(@Valid @RequestBody DepositRequestDTO request) throws BusinessException {
        return accountService.deposit(request);
    }

    @PostMapping("/transfer")
    public TransferResponseDTO transfer(@Valid @RequestBody TransferRequestDTO request) throws BusinessException {
        return accountService.transfer(request);
    }

}
