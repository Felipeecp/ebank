package io.github.felipeecp.ebank.service;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.mapper.TransactionMapper;
import io.github.felipeecp.ebank.model.dto.AccountBalanceDTO;
import io.github.felipeecp.ebank.model.dto.TransactionDTO;
import io.github.felipeecp.ebank.model.entity.Account;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.model.enums.TransactionType;
import io.github.felipeecp.ebank.repository.AccountRepository;
import io.github.felipeecp.ebank.repository.CustomerRepository;
import io.github.felipeecp.ebank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final AccountRepository accountRepository;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository, TransactionMapper transactionMapper, AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void processTransaction(TransactionDTO dto) throws BusinessException {
        Account account = accountRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));

        if(dto.type() == TransactionType.DEBIT){
            BigDecimal balance = transactionRepository.calculateBalance(dto.accountNumber());
            if(balance.compareTo(dto.amount()) < 0){
                throw new BusinessException("Saldo insuficiente");
            }
        }

        Transaction transaction = transactionMapper.toEntity(dto,account);
        transactionRepository.save(transaction);
    }

    public AccountBalanceDTO getBalance(String accountNumber) throws BusinessException {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Conta não encontrada"));

        BigDecimal balance = transactionRepository.calculateBalance(accountNumber);

        return new AccountBalanceDTO(
                accountNumber,
                balance,
                account.getCustomer().getName()
        );
    }

    public List<Transaction> getTransactions(String accountNumber) throws BusinessException {
        if(!accountRepository.existsByAccountNumber(accountNumber)){
            throw new BusinessException("Cliente não encontrado");
        }

        return transactionRepository.findByAccountNumberWithDetails(accountNumber);
    }


}
