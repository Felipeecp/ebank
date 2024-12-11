package io.github.felipeecp.ebank.service;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.mapper.CustomerMapper;
import io.github.felipeecp.ebank.model.dto.CustomerDTO;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.repository.CustomerRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerService(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Transactional
    public CustomerDTO createCustomer(CustomerDTO dto) throws BusinessException {
        if(customerRepository.existsByEmail(dto.email())){
            throw new BusinessException("Email já registrado");
        }

        Customer customer = customerMapper.toEntity(dto);
        customer.setAccountNumber(generateAccountNumber());

        return customerMapper.toDTO(customerRepository.save(customer));
    }

    public String generateAccountNumber(){
        return UUID.randomUUID().toString().substring(0,0);
    }

    public CustomerDTO findByAccountNumber(String accountNumber) throws BusinessException {
        return customerRepository.findByAccountNumber(accountNumber)
                .map(customerMapper::toDTO)
                .orElseThrow(()->new BusinessException("Cliente não encontrado"));
    }

}
