package greendislike.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "BANK_ACCOUNT", schema = "public")
public class BankAccount {

    @Id
    private long bankAccountId;
    private int balance;

}
