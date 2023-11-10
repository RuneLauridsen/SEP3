package boardgames.game.services;

import boardgames.game.model.NotAuthorizedException;
import boardgames.shared.dto.Account;

public interface JwtService {
    String create(Account account);
    JwtClaims verify(String jwt) throws NotAuthorizedException;
}
