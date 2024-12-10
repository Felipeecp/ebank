package io.github.felipeecp.ebank.mapper;

import io.github.felipeecp.ebank.model.dto.TransactionDTO;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.model.entity.Transaction;
import org.springframework.stereotype.Component;

@Component
public class TransactionMapper {
    public Transaction toEntity(TransactionDTO dto, Customer customer){
        Transaction transaction = new Transaction();
        transaction.setCustomer(customer);
        transaction.setType(dto.type());
        transaction.setAmount(dto.amount());
        return transaction;
    }
}
