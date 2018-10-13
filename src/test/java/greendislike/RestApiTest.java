package greendislike;

import greendislike.persistence.model.BankAccount;
import greendislike.persistence.repository.BankAccountRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private BankAccountRepository bankAccountRepository;

    @After
    public void clean() {
        bankAccountRepository.deleteAll();
    }

    @Test
    public void postCreateOk() {
        ResponseEntity<String> response = restTemplate.postForEntity("/bankaccount/12345", null, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.OK,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"bankAccountId\":12345,\"balance\":0}",
                response.getBody());
    }

    @Test
    public void postCreateWrongLengthId() {
        ResponseEntity<String> response = restTemplate.postForEntity("/bankaccount/1234", null, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"description\":\"Bank account id should be identified by exactly 5 digits\"}",
                response.getBody());
    }

    @Test
    public void getBalanceOk() {

        bankAccountRepository.save(new BankAccount(12345, 500));

        ResponseEntity<String> response = restTemplate.getForEntity("/bankaccount/12345/balance", String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.OK,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"bankAccountId\":12345,\"balance\":500}",
                response.getBody());
    }

    @Test
    public void getBalanceNotFoundException() {
        ResponseEntity<String> response = restTemplate.getForEntity("/bankaccount/12345/balance", String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.NOT_FOUND,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"description\":\"Could not find bank account with id = 12345\"}",
                response.getBody());
    }

}