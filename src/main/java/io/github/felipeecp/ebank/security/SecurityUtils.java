package io.github.felipeecp.ebank.security;

import io.github.felipeecp.ebank.exception.BusinessException;
import io.github.felipeecp.ebank.repository.AccountRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    private final AccountRepository accountRepository;

    public SecurityUtils(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public String getCurrentUserEmail() throws BusinessException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            throw new BusinessException("Usuário não autenticado");
        }

        Object principal = authentication.getPrincipal();
        if(principal instanceof UserDetails){
            return ((UserDetails) principal).getUsername();
        }

        throw new BusinessException("Usuàrio não encontrado");
    }

    public void validateAccountOwnership(String accountNumber, String userEmail) throws BusinessException {
        if (!accountRepository.isAccountOwner(accountNumber, userEmail)) {
            throw new BusinessException("Acesso não autorizado a esta conta");
        }
    }
}
