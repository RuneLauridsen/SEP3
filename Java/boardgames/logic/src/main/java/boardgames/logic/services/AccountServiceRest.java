package boardgames.logic.services;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.Empty;
import boardgames.shared.util.Log;
import boardgames.shared.dto.RegisterAccountParam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.List;

import static boardgames.logic.services.RestUtil.*;

public class AccountServiceRest implements AccountService {

    private final String url;
    private final RestTemplate restTemplate;

    public AccountServiceRest(String url) {
        this.url = url;
        this.restTemplate = new RestTemplate();
    }

    @Override
    public Account get(int accountId) {
        try {
            ResponseEntity<Account> response = restTemplate.getForEntity(url + "/accounts/" + accountId, Account.class);
            return getBodyOrThrow(response);
        } catch (RestClientException e) {
            Log.error(e);
            return Empty.account();
        }
    }

    @Override
    public Account get(String username) {
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(url + "/accounts?username=" + username, Account[].class);
            Account[] body = getBodyOrThrow(response);
            if (body.length == 0) {
                return Empty.account();
            } else {
                return body[0];
            }
        } catch (RestClientException e) {
            Log.error(e);
            return Empty.account();
        }
    }

    @Override
    public Account get(String username, String hashedPassword) {
        username = URLEncoder.encode(username, Charset.defaultCharset());
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(url + "/accounts?username=" + username + "&hashedPassword=" + hashedPassword, Account[].class);
            Account[] body = getBodyOrThrow(response);
            if (body.length == 0) {
                return Empty.account();
            } else {
                return coalesce(body[0], Empty.account());
            }
        } catch (RestClientException e) {
            Log.error(e);
            return Empty.account();
        }
    }

    @Override
    public Account create(RegisterAccountParam param) {
        try {
            ResponseEntity<Account> response = restTemplate.postForEntity(url + "/accounts", param, Account.class);
            Account body = getBodyOrThrow(response);
            return body;
        } catch (RestClientException e) {
            return Empty.account();
        }
    }

    @Override
    public List<Account> get() {
        try {
            ResponseEntity<Account[]> response = restTemplate.getForEntity(url + "/accounts", Account[].class);
            Account[] body = getBodyOrThrow(response);
            return List.of(body);
        } catch (RestClientException e) {
            Log.error(e);
            return List.of();
        }
    }

    @Override
    public boolean update(Account account) {
        try {
            restTemplate.put(url + "/accounts/" + account.accountId(), account);
            return true;
        } catch (RestClientException e) {
            Log.error(e);
            return false;
        }
    }
}
