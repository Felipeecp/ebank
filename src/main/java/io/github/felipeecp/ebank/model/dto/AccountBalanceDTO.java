package io.github.felipeecp.ebank.model.dto;

import java.math.BigDecimal;

public record AccountBalanceDTO(
        String accountNumber,
        BigDecimal balance,
        String customerName
){

}