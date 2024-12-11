package io.github.felipeecp.ebank.service;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.model.dto.AuthRequestDTO;
import io.github.felipeecp.ebank.model.dto.AuthResponseDTO;
import io.github.felipeecp.ebank.model.dto.RegisterRequestDTO;
import io.github.felipeecp.ebank.model.entity.Customer;
import io.github.felipeecp.ebank.model.entity.User;
import io.github.felipeecp.ebank.repository.CustomerRepository;
import io.github.felipeecp.ebank.repository.UserRepository;
import io.github.felipeecp.ebank.security.JwtTokenProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final CustomerRepository customerRepository;
    private final CustomerService customerService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtTokenProvider tokenProvider, CustomerRepository customerRepository, CustomerService customerService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.customerRepository = customerRepository;
        this.customerService = customerService;
    }

    @Transactional
    public void register(RegisterRequestDTO request) throws BusinessException {
        if(userRepository.existsByEmail(request.email())){
            throw new BusinessException("Email já existe");
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setRole("USER");

        userRepository.save(user);

        Customer customer = new Customer();
        customer.setName(request.name());
        customer.setAge(request.age());
        customer.setEmail(request.email());
        customer.setAccountNumber(customerService.generateAccountNumber());
        customer.setUser(user);

        customerRepository.save(customer);
    }

    public AuthResponseDTO login(AuthRequestDTO request) throws BusinessException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        String token = tokenProvider.createToken(authentication);
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(()->new BusinessException("Usuário não encontrado"));

        return new AuthResponseDTO(token, user.getEmail(), user.getCustomer().getName(), user.getCustomer().getAccountNumber());
    }

}
