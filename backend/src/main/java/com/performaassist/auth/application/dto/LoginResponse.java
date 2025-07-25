package com.performaassist.auth.application.dto;

import com.performaassist.auth.domain.model.UserRole;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour les réponses de connexion
 * 
 * Cette classe contient les informations retournées après une authentification
 * réussie, incluant le token JWT et les données utilisateur.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private long expiresIn;
    private UserInfo user;

    // Constructeurs
    public LoginResponse() {}

    public LoginResponse(String accessToken, String refreshToken, long expiresIn, UserInfo user) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.user = user;
    }

    // Getters et Setters
    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public UserInfo getUser() {
        return user;
    }

    public void setUser(UserInfo user) {
        this.user = user;
    }

    /**
     * Classe interne pour les informations utilisateur
     */
    public static class UserInfo {
        private UUID id;
        private String email;
        private String firstName;
        private String lastName;
        private UserRole role;
        private UUID siteId;
        private LocalDateTime lastLoginAt;

        // Constructeurs
        public UserInfo() {}

        public UserInfo(UUID id, String email, String firstName, String lastName, 
                       UserRole role, UUID siteId, LocalDateTime lastLoginAt) {
            this.id = id;
            this.email = email;
            this.firstName = firstName;
            this.lastName = lastName;
            this.role = role;
            this.siteId = siteId;
            this.lastLoginAt = lastLoginAt;
        }

        // Getters et Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public UserRole getRole() {
            return role;
        }

        public void setRole(UserRole role) {
            this.role = role;
        }

        public UUID getSiteId() {
            return siteId;
        }

        public void setSiteId(UUID siteId) {
            this.siteId = siteId;
        }

        public LocalDateTime getLastLoginAt() {
            return lastLoginAt;
        }

        public void setLastLoginAt(LocalDateTime lastLoginAt) {
            this.lastLoginAt = lastLoginAt;
        }

        public String getFullName() {
            return firstName + " " + lastName;
        }
    }
}

