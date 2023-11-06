package boardgames.game.services;

import boardgames.shared.dto.Account;

public interface AccountService {
    public Account get(int accountId);
    public Account get(String username);
    public Account get(String username, String hashedPassword);
}