package boardgames.logic.services;

import boardgames.logic.model.NotAuthorizedException;
import boardgames.shared.dto.Account;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;
import java.util.UUID;

// NOTE(rune): Baseret på https://www.baeldung.com/java-auth0-jwt

public class JwtServiceAuth0 implements JwtService {
    private static final String SECRET = "voresdatabaseergod";
    private static final String ISSUER = "VIA Boardgames";

    private static Algorithm encrypter;
    private static JWTVerifier decrypter;

    public JwtServiceAuth0() {
        encrypter = Algorithm.HMAC256(SECRET);
        decrypter = JWT.require(encrypter)
            .withIssuer("VIA Boardgames")
            .build();
    }

    @Override
    public String create(Account account) {
        String token = JWT.create()
            .withIssuer(ISSUER)
            //.withSubject("Baeldung Details")
            .withClaim("userId", account.accountId())
            .withClaim("username", account.username())
            .withClaim("isAdmin", account.isAdmin())
            .withIssuedAt(new Date())
            .withExpiresAt(new Date(System.currentTimeMillis() + 1000L * 60L * 20L)) // NOTE(rune): 20 minutter.
            .withJWTId(UUID.randomUUID().toString())
            //.withNotBefore(new Date(System.currentTimeMillis() + 1000L))
            .sign(encrypter);

        return token;
    }

    @Override
    public Claims verify(String jwt) throws NotAuthorizedException {
        try {
            DecodedJWT decoded = decrypter.verify(jwt);
            Claims claims = new Claims(
                decoded.getClaim("userId").asInt(),
                decoded.getClaim("username").asString(),
                decoded.getClaim("isAdmin").asBoolean()
            );
            return claims;
        } catch (JWTVerificationException e) {
            throw new NotAuthorizedException("Could not verify JWT token.");
        }
    }
}
