package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.shared.dto.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public Account get(@RequestParam(required = true) String username, @RequestParam(required = false) String hashedPassword) {
        Account account = null;
        if (hashedPassword == null) {
            account = accountData.get(username);
        } else {
            account = accountData.get(username, hashedPassword);
        }
        throwIfNotFound(username, account);
        return account;
    }
}
