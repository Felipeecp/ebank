package io.github.felipeecp.ebank.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.github.felipeecp.ebank.model.enums.AccountStatus;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @Column(name = "account_number")
    private String accountNumber;

    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "balance", nullable = false)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "credit_limit", nullable = false)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Column(name = "used_credit", nullable = false)
    private BigDecimal usedCredit = BigDecimal.ZERO;

    @Column(name = "daily_transfer_limit", nullable = false)
    private BigDecimal dailyTransferLimit = new BigDecimal("1000.00");

    @Column(name = "total_transferred_today", nullable = false)
    private BigDecimal totalTransferredToday = BigDecimal.ZERO;

    @Column(name = "last_transfer_date")
    private LocalDateTime lastTransferDate;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private AccountStatus status = AccountStatus.ACTIVE;

    public Account() {
    }

    public Account(String accountNumber, Customer customer, BigDecimal balance, BigDecimal creditLimit, BigDecimal usedCredit, BigDecimal dailyTransferLimit, BigDecimal totalTransferredToday, LocalDateTime lastTransferDate, AccountStatus status) {
        this.accountNumber = accountNumber;
        this.customer = customer;
        this.balance = balance;
        this.creditLimit = creditLimit;
        this.usedCredit = usedCredit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.totalTransferredToday = totalTransferredToday;
        this.lastTransferDate = lastTransferDate;
        this.status = status;
    }

    public BigDecimal getAvailableCredit() {
        return creditLimit.subtract(usedCredit);
    }

    public BigDecimal getTotalAvailable() {
        return balance.add(getAvailableCredit());
    }

    public void resetDailyTransferLimit() {
        this.totalTransferredToday = BigDecimal.ZERO;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getUsedCredit() {
        return usedCredit;
    }

    public void setUsedCredit(BigDecimal usedCredit) {
        this.usedCredit = usedCredit;
    }

    public BigDecimal getDailyTransferLimit() {
        return dailyTransferLimit;
    }

    public void setDailyTransferLimit(BigDecimal dailyTransferLimit) {
        this.dailyTransferLimit = dailyTransferLimit;
    }

    public BigDecimal getTotalTransferredToday() {
        return totalTransferredToday;
    }

    public void setTotalTransferredToday(BigDecimal totalTransferredToday) {
        this.totalTransferredToday = totalTransferredToday;
    }

    public LocalDateTime getLastTransferDate() {
        return lastTransferDate;
    }

    public void setLastTransferDate(LocalDateTime lastTransferDate) {
        this.lastTransferDate = lastTransferDate;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }
}
