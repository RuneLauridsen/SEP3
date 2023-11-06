package boardgames.persistence.controllers;

import boardgames.persistence.data.DataAccess;
import boardgames.shared.dto.Account;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static boardgames.persistence.controllers.ControllerUtil.*;

@RestController
public class AccountController {
    private final DataAccess dataAccess;

    public AccountController(DataAccess dataAccess) {
        this.dataAccess = dataAccess;
    }

    @GetMapping("accounts/{accountId}")
    public Account get(@PathVariable int accountId) {
        Account account = dataAccess.getAccount(accountId);
        throwIfNotFound(accountId, account);
        return account;
    }

    @GetMapping("/accounts")
    public Account get(@RequestParam(required = true) String username, @RequestParam(required = false) String hashedPassword) {
        Account account = null;
        if (hashedPassword == null) {
            account = dataAccess.getAccount(username);
        } else {
            account = dataAccess.getAccount(username, hashedPassword);
        }
        throwIfNotFound(username, account);
        return account;
    }
}
