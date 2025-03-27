package com.example.service;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import com.example.entity.Account;
import com.example.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AccountService {
    public enum RegisterStatus {
        SUCCESS,
        DUPLICATE,
        INVALID
    }
    @Autowired
    private AccountRepository accountRepository;
    public Map<String, Object> register(Account account) {
        Map<String, Object> result = new HashMap<>();
        if (account.getUsername() == null || account.getUsername().isBlank()
                || account.getPassword() == null || account.getPassword().length() < 4) {
            result.put("status", RegisterStatus.INVALID);
            return result;
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()) {
            result.put("status", RegisterStatus.DUPLICATE);
            return result;
        }
        Account saved = accountRepository.save(account);
        result.put("status", RegisterStatus.SUCCESS);
        result.put("account", saved);
        return result;
    }
    public Optional<Account> login(Account login) {
        return accountRepository.findByUsername(login.getUsername())
                .filter(a -> a.getPassword().equals(login.getPassword()));
    }
}