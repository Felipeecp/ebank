package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.AccountBalanceDTO;
import io.github.felipeecp.ebank.model.dto.TransactionDTO;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
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
}
