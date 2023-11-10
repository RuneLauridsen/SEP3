package boardgames.persistence.data;

import boardgames.shared.dto.Account;

public interface AccountData {
    public Account get(int accountId);
    public Account get(String username);
    public Account get(String username, String hashedPassword);
    // TODO(rune): update()
    // TODO(rune): delete()

}
