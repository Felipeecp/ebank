package io.github.felipeecp.ebank.repository;

import io.github.felipeecp.ebank.model.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByAccountNumber(String accountNumber);
    Optional<Customer> findByEmail(String email);
    boolean existsByAccountNumber(String accountNumber);
    boolean existsByEmail(String email);
}
