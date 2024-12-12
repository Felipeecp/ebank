package io.github.felipeecp.ebank.service;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.mapper.TransactionMapper;
import io.github.felipeecp.ebank.model.dto.*;
import io.github.felipeecp.ebank.model.entity.Account;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.model.enums.AccountStatus;
import io.github.felipeecp.ebank.model.enums.TransactionType;
import io.github.felipeecp.ebank.model.enums.TransferType;
import io.github.felipeecp.ebank.repository.AccountRepository;
import io.github.felipeecp.ebank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static java.util.stream.Collectors.toList;

@Service
public class AccountService {
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    private static final BigDecimal MINIMUM_DEPOSIT = new BigDecimal("10.00");
    private static final BigDecimal MAXIMUM_CREDIT_LIMIT = new BigDecimal("10000.00");

    public AccountService(AccountRepository accountRepository, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public DepositResponseDTO deposit(DepositRequestDTO request) throws BusinessException {
        Account account = findAndValidateAccount(request.accountNumber());
        validateDeposit(request.amount());

        account.setBalance(account.getBalance().add(request.amount()));
        accountRepository.save(account);

        // Registrar transação
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(request.amount());
        transaction.setDescription("Depósito em conta");
        transactionRepository.save(transaction);

        return new DepositResponseDTO(
                "Depósito realizado com sucesso",
                account.getBalance()
        );
    }

    @Transactional
    public CreditLimitResponseDTO adjustCreditLimit(CreditLimitRequestDTO request) throws BusinessException {
        Account account = findAndValidateAccount(request.accountNumber());

        validateCreditLimit(request.newCreditLimit());

        account.setCreditLimit(request.newCreditLimit());
        accountRepository.save(account);

        return new CreditLimitResponseDTO(
                account.getAccountNumber(),
                account.getCreditLimit(),
                account.getAvailableCredit()
        );
    }

    public List<AccountSummaryDTO> getAvailableAccounts(){
        return accountRepository.findByStatus(AccountStatus.ACTIVE)
                .stream()
                .map(account -> new AccountSummaryDTO(
                        account.getAccountNumber(),
                        account.getCustomer().getName()
                ))
                .toList();
    }

    @Transactional
    public TransferResponseDTO transfer(TransferRequestDTO request) throws BusinessException {
        Account fromAccount = findAndValidateAccount(request.fromAccountNumber());
        Account toAccount = findAndValidateAccount(request.toAccountNumber());

        validateTransfer(fromAccount, request.amount(), request.type());

        processTransfer(fromAccount, toAccount, request.amount(), request.type());

        // Registrar transação enviada
        Transaction debitTransaction = new Transaction();
        debitTransaction.setAccount(fromAccount);
        debitTransaction.setType(request.type() == TransferType.DEBIT ?
                TransactionType.TRANSFER_DEBIT : TransactionType.TRANSFER_CREDIT);
        debitTransaction.setAmount(request.amount());
        debitTransaction.setDescription("Transferência enviada");
        debitTransaction.setRelatedAccountNumber(toAccount.getAccountNumber());
        transactionRepository.save(debitTransaction);

        // Registrar transação recebida
        Transaction creditTransaction = new Transaction();
        creditTransaction.setAccount(toAccount);
        creditTransaction.setType(TransactionType.TRANSFER_RECEIVED);
        creditTransaction.setAmount(request.amount());
        creditTransaction.setDescription("Transferência recebida");
        creditTransaction.setRelatedAccountNumber(fromAccount.getAccountNumber());
        transactionRepository.save(creditTransaction);

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        return createTransferResponse(fromAccount,toAccount,request.amount());
    }

    public AccountStatementDTO getAccountStatement(String accountNumber,
                                                   LocalDateTime startDate,
                                                   LocalDateTime endDate) throws BusinessException {
        Account account = findAndValidateAccount(accountNumber);

        List<Transaction> transactions = transactionRepository.findByAccountAccountNumberAndDateTimeBetweenOrderByDateTimeDesc(
                accountNumber,
                startDate!=null?startDate:LocalDateTime.now().minusMonths(1),
                endDate!=null?endDate: LocalDateTime.now()
        );



        List<TransactionHistoryDTO> transactionDTOs = transactions.stream()
                .map(transactionMapper::mapToTransactionHistoryDTO)
                .toList();

        return new AccountStatementDTO(
                account.getAccountNumber(),
                account.getCustomer().getName(),
                account.getBalance(),
                transactionDTOs
        );
    }

    public AccountLimitsDTO getAcountLimits(String accountNumber) throws BusinessException {
        Account account = findAndValidateAccount(accountNumber);

        return new AccountLimitsDTO(
                account.getAccountNumber(),
                account.getBalance(),
                account.getCreditLimit(),
                account.getAvailableCredit(),
                account.getTotalAvailable()
        );
    }

    private TransferResponseDTO createTransferResponse(Account fromAccount, Account toAccount, BigDecimal amount) {
        return new TransferResponseDTO(
                UUID.randomUUID().toString(),
                LocalDateTime.now().toString(),
                new AccountTransferInfo(
                        fromAccount.getAccountNumber(),
                        fromAccount.getBalance(),
                        fromAccount.getAvailableCredit()
                ),
                new AccountTransferInfo(
                        toAccount.getAccountNumber(),
                        toAccount.getBalance(),
                        toAccount.getAvailableCredit()
                ),
                amount
        );
    }

    private void processTransfer(Account fromAccount, Account toAccount, BigDecimal amount, TransferType type) {
        if(type == TransferType.DEBIT){
            fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        }else{
            fromAccount.setUsedCredit(fromAccount.getUsedCredit().add(amount));
        }

        toAccount.setBalance(toAccount.getBalance().add(amount));

        fromAccount.setTotalTransferredToday(fromAccount.getTotalTransferredToday().add(amount));
        fromAccount.setLastTransferDate(LocalDateTime.now());
    }


    private void validateTransfer(Account fromAccount, BigDecimal amount, TransferType type) throws BusinessException {
        if(fromAccount.getTotalTransferredToday().add(amount).compareTo(fromAccount.getDailyTransferLimit())>0){
            throw new BusinessException("Limite diário de transferência excedido");
        }

        if(type == TransferType.DEBIT && fromAccount.getBalance().compareTo(amount) < 0){
            throw new BusinessException("Saldo insuficiente");
        }else if(type == TransferType.CREDIT && fromAccount.getAvailableCredit().compareTo(amount)<0){
            throw new BusinessException("Limite de crédito insuficiente");
        }

    }

    private void validateCreditLimit(BigDecimal newLimit) throws BusinessException {
        if(newLimit.compareTo(MAXIMUM_CREDIT_LIMIT) > 0){
            throw new BusinessException("Limite máximo de crédito é " + MAXIMUM_CREDIT_LIMIT);
        }
    }

    private void validateDeposit(BigDecimal amount) throws BusinessException {
        if(amount.compareTo(MINIMUM_DEPOSIT) < 0){
            throw new BusinessException("Valor mínimo para depósito é "+MINIMUM_DEPOSIT);
        }
    }

    private Account findAndValidateAccount(String accountNumber) throws BusinessException {
        return accountRepository.findByAccountNumber(accountNumber)
                .filter(account -> account.getStatus() == AccountStatus.ACTIVE)
                .orElseThrow(() -> new BusinessException("Conta não encontrada ou inativa"));
    }
}
