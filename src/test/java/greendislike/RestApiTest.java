package greendislike;

import greendislike.persistence.model.BankAccount;
import greendislike.persistence.model.BankAccountRequest;
import greendislike.persistence.repository.BankAccountRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@TestPropertySource(locations="classpath:test.properties")
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
    public void postCreateOkDatabase() {
        ResponseEntity<String> response = restTemplate.postForEntity("/bankaccount/12345", null, String.class);

        BankAccount bankAccount = bankAccountRepository.findById(12345L).orElse(null);

        Assert.assertNotNull(bankAccount);
        Assert.assertEquals(12345L, bankAccount.getBankAccountId());
        Assert.assertEquals(0, bankAccount.getBalance());
    }

    @Test
    public void postCreateWrongLengthId() {
        ResponseEntity<String> response = restTemplate.postForEntity("/bankaccount/1234", null, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"400\",\"description\":\"Bank account id should be identified by exactly 5 digits\"}",
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
                "{\"code\":\"404\",\"description\":\"Could not find bank account with id = 12345\"}",
                response.getBody());
    }

    @Test
    public void postCreateDuplicate() {

        bankAccountRepository.save(new BankAccount(12345, 500));

        ResponseEntity<String> response = restTemplate.postForEntity("/bankaccount/12345", null, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.CONFLICT,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"409\",\"description\":\"Bank account with id = 12345 exist\"}",
                response.getBody());
    }


    @Test
    public void putDepositOk() {
        bankAccountRepository.save(new BankAccount(12345, 500));

        HttpEntity<BankAccountRequest> requestEntity = new HttpEntity<>(new BankAccountRequest(500));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/deposit", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.OK,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"bankAccountId\":12345,\"balance\":1000}",
                response.getBody());
    }

    @Test
    public void putDepositEmptyBody() {
        bankAccountRepository.save(new BankAccount(12345, 500));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/deposit", HttpMethod.PUT,
                null, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"415\",\"description\":\"Content type '' not supported\"}",
                response.getBody());
    }

    @Test
    public void putDepositWrongRequest() {
        bankAccountRepository.save(new BankAccount(12345, 500));

        MultiValueMap mMap = new LinkedMultiValueMap();
        mMap.add("sum", "wrongType");
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(mMap);

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/deposit", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"415\",\"description\":\"Content type '' not supported\"}",
                response.getBody());
    }

    @Test
    public void putWithdrawOk() {
        bankAccountRepository.save(new BankAccount(12345, 500));

        HttpEntity<BankAccountRequest> requestEntity = new HttpEntity<>(new BankAccountRequest(500));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/withdraw", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.OK,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"bankAccountId\":12345,\"balance\":0}",
                response.getBody());
    }

    @Test
    public void putDepositNegativeRequest() {
        HttpEntity<BankAccountRequest> requestEntity = new HttpEntity<>(new BankAccountRequest(-500));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/deposit", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"400\",\"description\":\"sum: The value must be positive\"}",
                response.getBody());
    }

    @Test
    public void putWithdrawNegativeRequest() {
        HttpEntity<BankAccountRequest> requestEntity = new HttpEntity<>(new BankAccountRequest(-500));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/withdraw", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.BAD_REQUEST,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"400\",\"description\":\"sum: The value must be positive\"}",
                response.getBody());
    }

    @Test
    public void putWithdrawNegativeBalance() {
        bankAccountRepository.save(new BankAccount(12345, 500));

        HttpEntity<BankAccountRequest> requestEntity = new HttpEntity<>(new BankAccountRequest(1000));

        ResponseEntity<String> response = restTemplate.exchange("/bankaccount/12345/withdraw", HttpMethod.PUT,
                requestEntity, String.class);

        Assert.assertEquals("Server returned invalid HTTP code",
                HttpStatus.METHOD_NOT_ALLOWED,
                response.getStatusCode());

        Assert.assertEquals("Server returned invalid response",
                "{\"code\":\"405\",\"description\":\"Not enough balance = 500 on bank account with id = 12345\"}",
                response.getBody());
    }

}
