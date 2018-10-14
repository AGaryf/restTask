package greendislike.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountRequest {

    @Min(value = 0, message = "The value must be positive")
    private int sum;

}
