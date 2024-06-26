package com.banking.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.banking.model.Account;
import com.banking.service.AccountService;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    
    @Autowired
    private AccountService accountService;

    @GetMapping
    private List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/{id}")
    private Account getAccountById(@PathVariable Long id) {
        return accountService.getAccountById(id);
    }

    @PostMapping
    private ResponseEntity<Account> saveAccount(@RequestBody Account account) {
        Account newAccount = accountService.saveAccount(account);
        return ResponseEntity.status(HttpStatus.CREATED).body(newAccount);
    }

    @PostMapping("/{id}/deposit")
    public ResponseEntity<Account> deposit(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Account account = accountService.getAccountById(id);

        if (account == null) { 
            return ResponseEntity.notFound().build();
        } 

        Double amount = request.get("amount");
        account.setBalance(account.getBalance() + amount);
        Account accountUpdated = accountService.saveAccount(account);
        return ResponseEntity.ok(accountUpdated);
    }

    @PostMapping("/{id}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable Long id, @RequestBody Map<String, Double> request) {
        Account account = accountService.getAccountById(id);

        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta con ID " + id + " no encontrada");
        }

        Double amount = request.get("amount");
        double accountBalance = account.getBalance();

        if (amount > accountBalance ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accion denegada, montos insuficientes");
        }

        if (amount < 0 ) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Accion denegada, monto de retiro negativo");
        }

        account.setBalance(account.getBalance() - amount);
        Account accountUpdated = accountService.saveAccount(account);
        return ResponseEntity.ok(accountUpdated);
    }

    @PutMapping("/{id}")
    private ResponseEntity<?> updateAccount(@PathVariable Long id, @RequestBody Account account) {

        Account existingAccount = accountService.getAccountById(id);

        if (existingAccount != null) {
            existingAccount.setAccountHolderName(account.getAccountHolderName());
            existingAccount.setBalance(account.getBalance());
            accountService.saveAccount(existingAccount);
            return ResponseEntity.ok(existingAccount);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta con ID " + id + " no encontrada");
        }
    }

    @DeleteMapping("/{id}")
    private ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        Account existingAccount = accountService.getAccountById(id); 

        if (existingAccount != null) {
            accountService.deleteAccount(id);
            return ResponseEntity.ok().body("Eliminado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cuenta con ID " + id + " no encontrada");
        }
    }
}
