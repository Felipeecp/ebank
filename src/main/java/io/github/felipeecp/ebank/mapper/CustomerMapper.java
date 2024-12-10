package io.github.felipeecp.ebank.mapper;

import io.github.felipeecp.ebank.model.dto.CustomerDTO;
import io.github.felipeecp.ebank.model.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDTO toDTO(Customer customer){
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getAge(),
                customer.getEmail(),
                customer.getAccountNumber()
        );
    }

    public Customer toEntity(CustomerDTO customerDTO){
        Customer customer = new Customer();
        customer.setName(customerDTO.name());
        customer.setAge(customerDTO.age());
        customer.setEmail(customerDTO.email());
        customer.setAccountNumber(customerDTO.accountNumber());
        return customer;
    }

    public Customer updateEntity(Customer customer, CustomerDTO dto){
        customer.setName(dto.name());
        customer.setAge(dto.age());
        customer.setEmail(dto.email());
        return customer;
    }

}
