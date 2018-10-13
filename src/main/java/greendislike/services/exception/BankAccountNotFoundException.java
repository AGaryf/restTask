package greendislike.services.exception;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(long bankAccountId) {
        super("Could not find bank account with id = " + bankAccountId);
    }

}
