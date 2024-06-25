package com.banking.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.banking.repository.AccountRepository;
import com.banking.model.Account;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    public Account saveAccount(Account account) {
        return accountRepository.save(account);
    }

    public String deleteAccount(Long id) {
        accountRepository.deleteById(id);
        return "Eliminado correctamente";
    }

}
