package greendislike.services;

import greendislike.services.exception.BankAccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import greendislike.persistence.model.BankAccount;
import greendislike.persistence.repository.BankAccountRepository;
import javax.validation.constraints.Pattern;

@Validated
@RestController
public class BankAccountContoller {

    @Autowired
    private BankAccountRepository repository;

    @PostMapping(value = "/bankaccount/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    BankAccount newBankAccount(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                               @PathVariable("id") String id) {
        Long idValue = Long.valueOf(id);
        BankAccount bankAccount = new BankAccount(idValue, 0);
        return repository.save(bankAccount);
    }

    @GetMapping("/bankaccount/{id}/balance")
    BankAccount balance(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                        @PathVariable("id") String id) {
        Long idValue = Long.valueOf(id);
        return repository.findById(idValue)
                .orElseThrow(() -> new BankAccountNotFoundException(idValue));
    }

    @PutMapping(value = "/bankaccount/{id}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccount deposit(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                        @PathVariable("id") String id,
                        @RequestBody BankAccount newBankAccount) {
        Long idValue = Long.valueOf(id);
        return repository.findById(idValue)
                .map(bankAccount -> {
                    bankAccount.setBalance(bankAccount.getBalance() + newBankAccount.getBalance());
                    return repository.save(bankAccount);
                })
                .orElseThrow(() -> new BankAccountNotFoundException(idValue));
    }

    @PutMapping(value = "/bankaccount/{id}/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccount withdraw(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                         @PathVariable("id") String id,
                         @RequestBody BankAccount newBankAccount) {
        Long idValue = Long.valueOf(id);
        return repository.findById(idValue)
                .map(bankAccount -> {
                    bankAccount.setBalance(bankAccount.getBalance() - newBankAccount.getBalance());
                    return repository.save(bankAccount);
                })
                .orElseThrow(() -> new BankAccountNotFoundException(idValue));
    }

}
