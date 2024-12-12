package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.*;
import io.github.felipeecp.ebank.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/accounts")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PutMapping("/credit-limit")
    public CreditLimitResponseDTO adjustCreditLimit(@Valid @RequestBody CreditLimitRequestDTO request) throws BusinessException {
        return accountService.adjustCreditLimit(request);
    }

    @GetMapping("/available")
    public List<AccountSummaryDTO> getAvailableAccounts(){
        return accountService.getAvailableAccounts();
    }

    @GetMapping("/{accountNumber}/limits")
    public AccountLimitsDTO getAccountsLimits(@PathVariable String accountNumber) throws BusinessException {
        return accountService.getAcountLimits(accountNumber);
    }

    @GetMapping("/{accountNumber}/statement")
    public ResponseEntity<AccountStatementDTO> getStatement(
            @PathVariable String accountNumber,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) throws BusinessException {
        return ResponseEntity.ok(accountService.getAccountStatement(accountNumber, startDate, endDate));
    }
}
