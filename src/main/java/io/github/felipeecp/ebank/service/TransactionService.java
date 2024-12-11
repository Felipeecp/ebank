package io.github.felipeecp.ebank.service;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.mapper.TransactionMapper;
import io.github.felipeecp.ebank.model.dto.AccountBalanceDTO;
import io.github.felipeecp.ebank.model.dto.TransactionDTO;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.model.entity.Transaction;
import io.github.felipeecp.ebank.model.enums.TransactionType;
import io.github.felipeecp.ebank.repository.CustomerRepository;
import io.github.felipeecp.ebank.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, CustomerRepository customerRepository, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
        this.transactionMapper = transactionMapper;
    }

    @Transactional
    public void processTransaction(TransactionDTO dto) throws BusinessException {
        Customer customer = customerRepository.findByAccountNumber(dto.accountNumber())
                .orElseThrow(()->new BusinessException("Não foi encontrado cliente associado a numero da conta"));

        if(dto.type() == TransactionType.DEBIT){
            BigDecimal balance = transactionRepository.calculateBalance(dto.accountNumber());
            if(balance.compareTo(dto.amount()) < 0){
                throw new BusinessException("Saldo insuficiente");
            }
        }

        Transaction transaction = transactionMapper.toEntity(dto,customer);
        transactionRepository.save(transaction);
    }

    public AccountBalanceDTO getBalance(String accountNumber) throws BusinessException {
        Customer customer = customerRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new BusinessException("Customer not found"));

        BigDecimal balance = transactionRepository.calculateBalance(accountNumber);

        return new AccountBalanceDTO(
                accountNumber,
                balance,
                customer.getName()
        );
    }

    public List<Transaction> getTransactions(String accountNumber) throws BusinessException {
        if(!customerRepository.existsByAccountNumber(accountNumber)){
            throw new BusinessException("Cliente não encontrado");
        }

        return transactionRepository.findByCustomerAccountNumberOrderByDateTimeDesc(accountNumber);
    }


}
