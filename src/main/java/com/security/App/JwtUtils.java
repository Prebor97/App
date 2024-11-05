package com.security.App;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@Component
public class JwtUtils {

    private String secretKey = "6f6c34a4945d4610fd47d6957cd0476a66e2cfa313f89444a05a6797e9d6bc12f6c8c36d538772047686535412a3479bfc417409a60d3d6193b0a4a69f2cab7d1a681d38c0c862498bf0816ce2ac5cb91ed861f354adee291e97a49341ad32ebfd3601d2b883788d53f2192ba33aa2878a9d5b3bf5b2dca9595db4852d361d04dec442dd190d99ad7c3721712d03e55371c2640f32ef2bae086ec32ad011df858bf1dcd108cfc7020127d970568acc046f6eafcd2fb3c54a4f0e1949af7338a2bb4129c6ecca01b28c8cc053df25d5db8969182396fab8a0d4787e8abe70bdba217c5566dbaff0978fc8ee042083f905f522fe361ec180d32d0b284722d47369";

    private final Supplier<SecretKeySpec> getKey =() -> {
        Key key = Keys.hmacShaKeyFor(secretKey
                .getBytes(StandardCharsets.UTF_8));
        return new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
    };

    private final Supplier<Date> expirationTime = ()-> Date
            .from(LocalDateTime.now()
                    .plusMinutes(60).atZone(ZoneId.systemDefault()).toInstant());
    private <T> T extractClaims(String token, Function<Claims, T> claimResolver){
        final Claims claims = Jwts.parser()
                .verifyWith(getKey.get())
                .build().parseSignedClaims(token).getPayload();
        return claimResolver.apply(claims);
    }

    public Function<String, String> extractUsername = token->
            extractClaims(token, Claims::getSubject);

    private final Function<String, Date> extractExpirationDate = token->
            extractClaims(token, Claims::getExpiration);

    public Function<String, Boolean> isTokenExpired = token->extractExpirationDate.apply(token)
            .after(new Date(System.currentTimeMillis()));

    public BiFunction<String, String, Boolean> isTokenValid = (token, username) ->
            isTokenExpired.apply(token)&& Objects.equals(extractUsername.apply(token), username);

    public Function<UserDetails, String> createJwt = userDetails -> {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(userDetails.getUsername())
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationTime.get())
                .compact();
    };
    public Function<String, String> createJwtForOAuth = username -> {
        Map<String, Object> claims = new HashMap<>();
        return Jwts.builder()
                .signWith(getKey.get())
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(expirationTime.get())
                .compact();
    };
}

