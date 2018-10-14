package greendislike.services;

import greendislike.persistence.model.BankAccountRequest;
import greendislike.services.exception.type.BankAccountDuplicateException;
import greendislike.services.exception.type.BankAccountNotEnoughException;
import greendislike.services.exception.type.BankAccountNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import greendislike.persistence.model.BankAccount;
import greendislike.persistence.repository.BankAccountRepository;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Validated
@RestController
public class BankAccountContoller {

    @Autowired
    private BankAccountRepository repository;

    @PostMapping(value = "/bankaccount/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    BankAccount newBankAccount(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                               @PathVariable("id") String idString) {
        Long id = Long.valueOf(idString);

        repository.findById(id)
                .ifPresent(bankAccount -> {
                    throw new BankAccountDuplicateException(id);
                });

        BankAccount bankAccount = new BankAccount(id, 0);
        return repository.save(bankAccount);
    }

    @GetMapping("/bankaccount/{id}/balance")
    BankAccount balance(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                        @PathVariable("id") String idString) {
        Long id = Long.valueOf(idString);
        return repository.findById(id)
                .orElseThrow(() -> new BankAccountNotFoundException(id));
    }

    @PutMapping(value = "/bankaccount/{id}/deposit", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccount deposit(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                        @PathVariable("id") String idString,
                        @Valid @RequestBody BankAccountRequest newBankAccountRequest) {
        Long id = Long.valueOf(idString);
        return repository.findById(id)
                .map(bankAccount -> {
                    bankAccount.setBalance(bankAccount.getBalance() + newBankAccountRequest.getSum());
                    return repository.save(bankAccount);
                })
                .orElseThrow(() -> new BankAccountNotFoundException(id));
    }

    @PutMapping(value = "/bankaccount/{id}/withdraw", consumes = MediaType.APPLICATION_JSON_VALUE)
    BankAccount withdraw(@Pattern(regexp = "\\d{5}", message = "Bank account id should be identified by exactly 5 digits")
                         @PathVariable("id") String idString,
                         @Valid @RequestBody BankAccountRequest newBankAccountRequest) {
        Long id = Long.valueOf(idString);
        return repository.findById(id)
                .map(bankAccount -> {
                    if (bankAccount.getBalance() < newBankAccountRequest.getSum()) {
                        throw new BankAccountNotEnoughException(bankAccount);
                    }
                    bankAccount.setBalance(bankAccount.getBalance() - newBankAccountRequest.getSum());
                    return repository.save(bankAccount);
                })
                .orElseThrow(() -> new BankAccountNotFoundException(id));
    }

}
