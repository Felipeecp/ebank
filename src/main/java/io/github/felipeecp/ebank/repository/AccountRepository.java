package io.github.felipeecp.ebank.repository;

import io.github.felipeecp.ebank.model.entity.Account;
import io.github.felipeecp.ebank.model.enums.AccountStatus;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, String> {

    Optional<Account> findByAccountNumber(String accountNumber);

    boolean existsByAccountNumber(String accountNumber);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT a FROM Account a WHERE a.accountNumber = :accountNumber")
    Optional<Account> findByAccountNumberWithLock(@Param("accountNumber") String accountNumber);

    List<Account> findByStatus(AccountStatus status);

    @Query("""
        SELECT CASE WHEN COUNT(a) > 0 THEN true ELSE false END 
        FROM Account a 
        WHERE a.accountNumber = :accountNumber 
        AND a.customer.user.email = :userEmail
    """)
    boolean isAccountOwner(@Param("accountNumber") String accountNumber, @Param("userEmail") String userEmail);
}
