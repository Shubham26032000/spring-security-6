package com.shubham.security.service;

import com.shubham.security.entity.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private String secretKey = null;

    public String generateToken(UserEntity user) {
        Map<String,Object> claims = new HashMap<>();
        return Jwts
                .builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuer("SHUBHAM")
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis()+ 60*10*1000))
                .and()
                .signWith(generateKey())
                .compact();
    }

    private SecretKey generateKey() {
        byte [] decode = Decoders.BASE64.decode(getSecretKey());
        return Keys.hmacShaKeyFor(decode);
    }

    public String getSecretKey(){
        return secretKey = "dc429dsdssdsfsfsfsfsfsfsfsfsfsffd5971a156b106e413dde08363c";
    }

    public String extractUserName(String jwtToken) {
        return extractClaims(jwtToken, Claims::getSubject);
    }

    private <T>T extractClaims(String jwtToken, Function<Claims,T> claimResolver) {
        Claims claims = extractClaims(jwtToken);
        return claimResolver.apply(claims);
    }

    private Claims extractClaims(String jwtToken) {
       return Jwts
                .parser()
                .verifyWith(generateKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        final String userName = extractUserName(jwtToken);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(jwtToken));
    }

    private boolean isTokenExpired(String jwtToken) {
        return extractExpiration(jwtToken).before(new Date());
    }

    private Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken,Claims::getExpiration);

    }
}
