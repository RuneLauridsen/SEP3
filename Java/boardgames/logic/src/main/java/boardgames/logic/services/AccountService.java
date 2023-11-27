package boardgames.logic.services;

import boardgames.shared.dto.Account;

import java.util.List;

public interface AccountService {
    public Account get(int accountId);
    public Account get(String username);
    public Account get(String username, String hashedPassword);
    public List<Account> get();

    boolean update(Account account);
}
