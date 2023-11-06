package boardgames.game.services;

import boardgames.shared.dto.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class AccountServiceRest implements AccountService {

    private final String ulr;
    private final RestTemplate restTemplate;

    public AccountServiceRest(String ulr) {
        this.ulr = ulr;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Account get(int accountId) {
        try {
            ResponseEntity<Account> response = restTemplate.getForEntity(ulr + "/accounts/" + accountId, Account.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public Account get(String username) {
        try {
            ResponseEntity<Account> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username, Account.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public Account get(String username, String hashedPassword) {
        // TODO(rune): url escape username
        try {
            ResponseEntity<Account> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username + "&hashedPassword=" + hashedPassword, Account.class);
            return response.getBody(); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }
}
