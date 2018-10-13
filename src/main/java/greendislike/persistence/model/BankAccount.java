package greendislike.persistence.model;

import lombok.Data;
import javax.persistence.*;

@Data
@Entity
@Table(name = "BANK_ACCOUNT", schema = "public")
public class BankAccount {

    public static final int ACCOUNT_ID_MAX_VALUE = 99999;
    public static final int ACCOUNT_ID_MIN_VALUE = 10000;

    @Id
    private long bankAccountId;
    private int balance;

    public BankAccount() {

    }

    public BankAccount(long bankAccountId, int balance) {
        this.bankAccountId = bankAccountId;
        this.balance = balance;
    }
}
