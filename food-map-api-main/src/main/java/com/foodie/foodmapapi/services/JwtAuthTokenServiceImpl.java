package com.foodie.foodmapapi.services;

import com.foodie.foodmapapi.exceptions.InvalidParameterException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtAuthTokenServiceImpl implements JwtAuthTokenService {

    @Value("${food-map-api.jwtauth.token.secret}")
    private String jwtAuthTokenSecret;

    @Value("${food-map-api.jwtauth.token.expiration}")
    private long jwtAuthTokenExpiration;

    public String generateToken(UserDetails userDetails) {
        return Jwts.builder()
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtAuthTokenExpiration))
                .signWith(SignatureAlgorithm.HS512, jwtAuthTokenSecret)
                .compact();
    }

    /*
    If the username is returned (not null) then the token is valid
    Throw token with description for better error messages to client
     */
    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRolesFromToken(String token) {
        return extractClaim(token, claims -> (List<String>) claims.get("roles"));
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(jwtAuthTokenSecret)
                    .parseClaimsJws(token)
                    .getBody();
        }
        catch(UnsupportedJwtException e) {
            throw new InvalidParameterException("Token does not represent a JWS Claim");
        }
        catch(MalformedJwtException e) {
            throw new InvalidParameterException("Token is not a valid JWS Claim");
        }
        catch(SignatureException e) {
            throw new InvalidParameterException("Token signature validation failed");
        }
        catch(ExpiredJwtException e) {
            throw new InvalidParameterException("Token has expired");
        }
        catch(IllegalArgumentException e) {
            throw new InvalidParameterException("Token is null or empty");
        }
    }
}
