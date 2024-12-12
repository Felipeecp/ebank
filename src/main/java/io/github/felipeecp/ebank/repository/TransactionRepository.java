package io.github.felipeecp.ebank.repository;

import io.github.felipeecp.ebank.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t JOIN FETCH t.account a WHERE a.accountNumber = :accountNumber ORDER BY t.dateTime DESC")
    List<Transaction> findByAccountNumberWithDetails(@Param("accountNumber") String accountNumber);

    @Query("""
        SELECT COALESCE(SUM(CASE WHEN t.type = 'CREDIT' THEN t.amount ELSE -t.amount END), 0)
        FROM Transaction t
        WHERE t.account.accountNumber = :accountNumber
    """)
    BigDecimal calculateBalance(@Param("accountNumber") String accountNumber);

    List<Transaction> findByAccountAccountNumberAndDateTimeBetweenOrderByDateTimeDesc(
            String accountNumber,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}