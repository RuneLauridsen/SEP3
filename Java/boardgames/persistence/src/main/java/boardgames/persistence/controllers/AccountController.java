package boardgames.persistence.controllers;

import boardgames.persistence.data.AccountData;
import boardgames.shared.dto.Account;
import org.springframework.web.bind.annotation.*;

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
        Account account = accountData.getWithPicture(accountId);
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

    @PutMapping("/accounts/{accountId}")
    public void update(@PathVariable int accountId, @RequestBody Account account) {
        throwIfMismatched(accountId, account);
        accountData.update(account);
    }

    public List<Account> getAll() {
        List<Account> accounts = accountData.getAll();
        return accounts;
    }
}
