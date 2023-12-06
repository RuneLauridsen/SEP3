package boardgames.logic.services;

import boardgames.shared.dto.Account;
import boardgames.shared.util.Log;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import static boardgames.logic.services.RestUtil.*;

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
            return getBodyOrThrow(response);
        } catch (RestClientException e) {
            Log.logError(e);
            return null;
        }
    }

    @Override
    public Account get(String username) {
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username, Account[].class);
            Account[] body = getBodyOrThrow(response);
            if (body.length == 0) {
                return null;
            } else {
                return body[0];
            }
        } catch (RestClientException e) {
            Log.logError(e);
            return null;
        }
    }

    @Override
    public Account get(String username, String hashedPassword) {
        username = URLEncoder.encode(username, Charset.defaultCharset());
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts?username=" + username + "&hashedPassword=" + hashedPassword, Account[].class);
            Account[] body = getBodyOrThrow(response);
            if (body.length == 0) {
                return null;
            } else {
                return body[0];
            }
        } catch (RestClientException e) {
            Log.logError(e);
            return null;
        }
    }

    @Override
    public List<Account> get() {
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(ulr + "/accounts", Account[].class);
            return List.of(getBodyOrThrow(response));
        } catch (RestClientException e) {
            Log.logError(e);
            return null;
        }
    }

    @Override
    public boolean update(Account account) {
        try {
            restTemplate.put(ulr + "/accounts/" + account.accountId(), account);
            return true;
        } catch (RestClientException e) {
            Log.logError(e);
            return false;
        }
    }
}
