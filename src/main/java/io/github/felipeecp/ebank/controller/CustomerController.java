package io.github.felipeecp.ebank.controller;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.CustomerDTO;
import io.github.felipeecp.ebank.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {
    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CustomerDTO createCustomer(@Valid @RequestBody CustomerDTO customerDTO) throws BusinessException {
        return customerService.createCustomer(customerDTO);
    }

    @GetMapping("/{accountNumber}")
    public CustomerDTO getCustomer(@PathVariable String accountNumber) throws BusinessException {
        return customerService.findByAccountNumber(accountNumber);
    }

}
