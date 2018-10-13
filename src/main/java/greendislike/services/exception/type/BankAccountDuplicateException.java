package greendislike.services.exception.type;

public class BankAccountDuplicateException extends RuntimeException {

    public BankAccountDuplicateException(long id) {
        super("Bank account with id = " + id + " exist");
    }

}
