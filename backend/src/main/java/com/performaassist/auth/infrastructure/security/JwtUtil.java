package com.performaassist.auth.infrastructure.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * Utilitaire pour la gestion des tokens JWT
 * 
 * Cette classe fournit les méthodes pour créer, valider et extraire
 * les informations des tokens JWT utilisés pour l'authentification.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Component
public class JwtUtil {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    @Value("${spring.security.jwt.secret}")
    private String jwtSecret;

    @Value("${spring.security.jwt.expiration}")
    private long jwtExpirationMs;

    @Value("${spring.security.jwt.refresh-expiration}")
    private long jwtRefreshExpirationMs;

    /**
     * Génère un token d'accès JWT
     */
    public String generateAccessToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUserId(userPrincipal.getId(), jwtExpirationMs);
    }

    /**
     * Génère un token de rafraîchissement JWT
     */
    public String generateRefreshToken(Authentication authentication) {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return generateTokenFromUserId(userPrincipal.getId(), jwtRefreshExpirationMs);
    }

    /**
     * Génère un token JWT à partir d'un ID utilisateur
     */
    public String generateTokenFromUserId(UUID userId, long expiration) {
        Date expiryDate = new Date(System.currentTimeMillis() + expiration);

        return Jwts.builder()
                .setSubject(userId.toString())
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extrait l'ID utilisateur du token JWT
     */
    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    /**
     * Valide un token JWT
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token);
            return true;
        } catch (MalformedJwtException e) {
            logger.error("Token JWT malformé: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("Token JWT expiré: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("Token JWT non supporté: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("Chaîne JWT vide: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Erreur de validation du token JWT: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Obtient la date d'expiration d'un token
     */
    public Date getExpirationDateFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getExpiration();
    }

    /**
     * Vérifie si un token est expiré
     */
    public boolean isTokenExpired(String token) {
        Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    /**
     * Obtient la durée d'expiration du token d'accès en millisecondes
     */
    public long getAccessTokenExpirationMs() {
        return jwtExpirationMs;
    }

    /**
     * Obtient la durée d'expiration du token de rafraîchissement en millisecondes
     */
    public long getRefreshTokenExpirationMs() {
        return jwtRefreshExpirationMs;
    }

    /**
     * Génère la clé de signature à partir du secret
     */
    private SecretKey getSigningKey() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}

