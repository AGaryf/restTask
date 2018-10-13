package greendislike.services.exception.type;

import greendislike.persistence.model.BankAccount;

public class BankAccountNotEnoughException extends RuntimeException {

    public BankAccountNotEnoughException(BankAccount bankAccount) {
        super("Not enough balance = " + bankAccount.getBalance()
                + " on bank account with id = " + bankAccount.getBankAccountId());
    }

}
