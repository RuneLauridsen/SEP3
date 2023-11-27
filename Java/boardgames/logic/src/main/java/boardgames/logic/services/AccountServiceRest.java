package boardgames.logic.services;

import boardgames.shared.dto.Account;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

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
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username, Account[].class);
            if (response.getBody().length == 0) {
                return null;
            } else {
                return response.getBody()[0];
            }
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public Account get(String username, String hashedPassword) {
        // TODO(rune): url escape username
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username + "&hashedPassword=" + hashedPassword, Account[].class);
            if (response.getBody().length == 0) {
                return null;
            } else {
                return response.getBody()[0];
            }
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public List<Account> get() {
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts", Account[].class);
            return List.of(response.getBody()); // TODO(rune): Check status code.
        } catch (RestClientException e) {
            throw new RuntimeException(e); // TODO(rune): Error handling.
        }
    }

    @Override
    public boolean updateStatus(Account account) {
        try{
            restTemplate.put(ulr+"/accounts/"+ account.accountId(), account);
            return true;
        } catch (RestClientException e) {
            throw new RuntimeException(e);
        }
    }
}
