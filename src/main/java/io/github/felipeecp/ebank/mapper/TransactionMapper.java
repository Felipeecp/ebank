package io.github.felipeecp.ebank.mapper;

import io.github.felipeecp.ebank.model.dto.TransactionDTO;
import io.github.felipeecp.ebank.model.dto.TransactionHistoryDTO;
import io.github.felipeecp.ebank.model.entity.Account;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction toEntity(TransactionDTO dto, Account customer){
        Transaction transaction = new Transaction();
        transaction.setAccount(customer);
        transaction.setType(dto.type());
        transaction.setAmount(dto.amount());
        return transaction;
    }

    public TransactionHistoryDTO mapToTransactionHistoryDTO(Transaction transaction) {
        return new TransactionHistoryDTO(
                transaction.getType().toString(),
                transaction.getAmount(),
                transaction.getDateTime(),
                transaction.getDescription(),
                transaction.getRelatedAccountNumber()
        );
    }
}
