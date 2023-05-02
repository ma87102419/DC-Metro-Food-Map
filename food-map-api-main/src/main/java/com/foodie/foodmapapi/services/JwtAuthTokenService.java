package com.foodie.foodmapapi.services;

import io.jsonwebtoken.Claims;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.function.Function;

public interface JwtAuthTokenService {

    String generateToken(UserDetails userDetails);

    String extractUsernameFromToken(String token);

    List<String> extractRolesFromToken(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimsResolver);

}
