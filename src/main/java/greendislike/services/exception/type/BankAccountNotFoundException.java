package greendislike.services.exception.type;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(long bankAccountId) {
        super("Could not find bank account with id = " + bankAccountId);
    }

}
