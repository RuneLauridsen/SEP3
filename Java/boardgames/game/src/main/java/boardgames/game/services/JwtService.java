package boardgames.game.services;

import boardgames.shared.dto.Account;

public interface JwtService {
    String create(Account account);
    JwtClaims verify(String jwt);
}
