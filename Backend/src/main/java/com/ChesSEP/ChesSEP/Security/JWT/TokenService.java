package com.ChesSEP.ChesSEP.Security.JWT;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;


@Service
public class TokenService {
    
    private final String KEY="qyYuBp3B6PUttQoE6ywZQc2R2lYQDeQ37aNg01GBF0iAQgSf/9/9QelRS+KVnkfW";

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver){
        Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts
            .parser()
            .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY)))
            .build()
            .parseSignedClaims(token)
            .getPayload();      
    }

    //leere map weil keine extra claims gemacht werden müssen
    public String GenerateToken(UserDetails userDetails){
        return GenerateToken(new HashMap<>(),userDetails);
    }

    public String GenerateToken(Map<String,Object>extraclaims,UserDetails userDetails){
        return Jwts
            .builder()
            .claims(extraclaims)
            .subject(userDetails.getUsername())
            .issuedAt(new Date(System.currentTimeMillis()))
            .expiration(new Date(System.currentTimeMillis()+ 1000*60*24))
            .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(KEY)),Jwts.SIG.HS256)
            .compact();
    }

    public boolean isTokenValid(UserDetails userDetails,String token){
        return extractEmail(token).equals(userDetails.getUsername())||extractAllClaims(token).getExpiration().before(new Date(System.currentTimeMillis()));
    }

    public String extractEmail(String token){
        return extractAllClaims(token).getSubject();
    }  
}
