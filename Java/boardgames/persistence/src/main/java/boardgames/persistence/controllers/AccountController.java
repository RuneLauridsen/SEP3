package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.shared.dto.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class AccountController {
    private final AccountData accountData;

    public AccountController(AccountData accountData) {
        this.accountData = accountData;
    }

    @GetMapping("accounts/{accountId}")
    public Account get(@PathVariable int accountId) {
        Account account = accountData.get(accountId);
        throwIfNotFound(accountId, account);
        return account;
    }

    @GetMapping("/accounts")
    public List<Account> get(@RequestParam(required = false) String username, @RequestParam(required = false) String hashedPassword) {
        List<Account> accounts = new ArrayList<>();
        if (username == null && hashedPassword == null) {
            accounts.addAll(getAll());
        } else if (hashedPassword == null) {
            accounts.add(accountData.get(username));
        } else {
            accounts.add(accountData.get(username, hashedPassword));
        }

        return accounts;
    }

    public List<Account> getAll() {
        List<Account> accounts = accountData.getAll();
        return accounts;
    }
}
