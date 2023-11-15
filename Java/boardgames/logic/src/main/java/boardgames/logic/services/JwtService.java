package boardgames.logic.services;

import boardgames.logic.model.NotAuthorizedException;
import boardgames.shared.dto.Account;

public interface JwtService {
    String create(Account account);
    JwtClaims verify(String jwt) throws NotAuthorizedException;
}
