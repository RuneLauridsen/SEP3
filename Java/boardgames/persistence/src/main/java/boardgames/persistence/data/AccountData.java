package boardgames.persistence.data;

import boardgames.shared.dto.Account;
import boardgames.shared.dto.RegisterAccountParam;

import java.util.List;

public interface AccountData {
    public Account get(int accountId);
    public Account getWithPicture(int accountId);
    public Account get(String username);
    public Account get(String username, String hashedPassword);
    public List<Account> getAll();
    public Account create (RegisterAccountParam param);
    public void update(Account account);
}
