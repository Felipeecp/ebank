package io.github.felipeecp.ebank.repository;

import io.github.felipeecp.ebank.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByCustomerAccountNumberOrderByDateTimeDesc(String accountNumber);

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE -t.amount END), 0)
        FROM Transaction t
        WHERE t.customer.accountNumber = :accountNumber
    """)
    BigDecimal calculateBalance(String accountNumber);

}
