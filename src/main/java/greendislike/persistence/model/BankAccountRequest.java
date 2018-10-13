package greendislike.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Min;

@Data
@AllArgsConstructor
public class BankAccountRequest {

    @Min(value = 0, message = "The value must be positive")
    private int balance;

}
