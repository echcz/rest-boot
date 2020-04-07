package cn.echcz.restboot.auth;

import cn.echcz.restboot.config.AuthProperties;
import cn.echcz.restboot.config.JwtProperties;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JwtHandler {
    @NonNull
    private final JwtProperties jwtProperties;
    @NonNull
    private final AuthProperties authProperties;


    public String createToken(Map<String, String> claims) {
        JWTCreator.Builder builder = getJwtCreatorBuilder();
        claims.forEach(builder::withClaim);
        return builder.sign(jwtProperties.getAlgorithm());
    }

    private JWTCreator.Builder getJwtCreatorBuilder() {
        return JWT.create()
                .withIssuer(jwtProperties.getIssuer())
                .withExpiresAt(new Date(System.currentTimeMillis() + jwtProperties.getMaxAge()));
    }

    public String createTokenByUserId(Integer userId) {
        JWTCreator.Builder builder = getJwtCreatorBuilder();
        builder.withClaim(authProperties.getJwtKey(), userId);
        return builder.sign(jwtProperties.getAlgorithm());
    }

    public Map<String, String> getClaimsFromToken(String token) throws JWTVerificationException {
        DecodedJWT jwt = verifyToken(token);
        Map<String, Claim> claims = jwt.getClaims();
        Map<String, String> result = new HashMap<>();
        claims.forEach((k, v) -> result.put(k, v.asString()));
        return result;
    }

    private DecodedJWT verifyToken(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT
                .require(jwtProperties.getAlgorithm())
                .withIssuer(jwtProperties.getIssuer())
                .build();
        return jwtVerifier.verify(token);
    }

    public Integer getUserIdFromToken(String token) throws JWTVerificationException{
        DecodedJWT jwt = verifyToken(token);
        return jwt.getClaim(authProperties.getJwtKey()).asInt();
    }

}
