package io.github.felipeecp.ebank.repository;

import io.github.felipeecp.ebank.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    @Query("SELECT c FROM Customer c JOIN FETCH c.account a JOIN FETCH c.user WHERE a.accountNumber = :accountNumber")
    Optional<Customer> findByAccountNumberWithUser(@Param("accountNumber") String accountNumber);

    Optional<Customer> findByEmail(String email);

    @Query("SELECT COUNT(c) > 0 FROM Customer c JOIN c.account a WHERE a.accountNumber = :accountNumber")
    boolean existsByAccountNumber(@Param("accountNumber") String accountNumber);

    boolean existsByEmail(String email);
}
