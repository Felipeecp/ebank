package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.CustomerDTO;
import io.github.felipeecp.ebank.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@Tag(name = "Clientes", description = "API para gerenciamento de clientes")
@SecurityRequirement(name = "bearerAuth")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Operation(
            summary = "Criar novo cliente",
            description = "Cadastra um novo cliente no sistema bancário"
    )
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(
            @Parameter(
                    description = "Dados do cliente a ser criado",
                    required = true
            )
            @Valid @RequestBody CustomerDTO customerDTO
    ) throws BusinessException {
        return customerService.createCustomer(customerDTO);
    }

    @Operation(
            summary = "Buscar cliente",
            description = "Recupera os dados de um cliente pelo número da conta"
    )
    @GetMapping("/{accountNumber}")
    public CustomerDTO getCustomer(
            @Parameter(
                    description = "Número da conta do cliente",
                    required = true,
                    example = "12345-6"
            )
            @PathVariable String accountNumber
    ) throws BusinessException {
        return customerService.findByAccountNumber(accountNumber);
    }
}