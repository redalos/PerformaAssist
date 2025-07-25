package com.performaassist.auth.application.usecase;

import com.performaassist.auth.application.dto.LoginRequest;
import com.performaassist.auth.application.dto.LoginResponse;
import com.performaassist.auth.domain.model.User;
import com.performaassist.auth.domain.repository.UserRepository;
import com.performaassist.auth.infrastructure.security.JwtUtil;
import com.performaassist.auth.infrastructure.security.UserPrincipal;
import com.performaassist.shared.exception.AuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service d'authentification
 * 
 * Ce service gère les cas d'usage liés à l'authentification :
 * - Connexion utilisateur
 * - Rafraîchissement des tokens
 * - Déconnexion
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Service
@Transactional
public class AuthenticationService {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationService.class);

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationService(AuthenticationManager authenticationManager,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder,
                               JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authentifie un utilisateur et génère les tokens JWT
     */
    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        try {
            // Authentification avec Spring Security
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Génération des tokens
            String accessToken = jwtUtil.generateAccessToken(authentication);
            String refreshToken = jwtUtil.generateRefreshToken(authentication);

            // Récupération des informations utilisateur
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            User user = userRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));

            // Mise à jour de la dernière connexion
            user.updateLastLogin();
            userRepository.save(user);

            // Construction de la réponse
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getSiteId(),
                user.getLastLoginAt()
            );

            LoginResponse response = new LoginResponse(
                accessToken,
                refreshToken,
                jwtUtil.getAccessTokenExpirationMs() / 1000, // en secondes
                userInfo
            );

            logger.info("Utilisateur authentifié avec succès: {}", user.getEmail());
            return response;

        } catch (Exception e) {
            logger.error("Erreur lors de l'authentification de l'utilisateur: {}", loginRequest.getEmail(), e);
            throw new AuthenticationException("Identifiants invalides");
        }
    }

    /**
     * Rafraîchit le token d'accès à partir du token de rafraîchissement
     */
    public LoginResponse refreshToken(String refreshToken) {
        try {
            if (!jwtUtil.validateToken(refreshToken)) {
                throw new AuthenticationException("Token de rafraîchissement invalide");
            }

            UUID userId = jwtUtil.getUserIdFromToken(refreshToken);
            User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));

            if (!user.getIsActive()) {
                throw new AuthenticationException("Compte utilisateur désactivé");
            }

            // Génération d'un nouveau token d'accès
            String newAccessToken = jwtUtil.generateTokenFromUserId(userId, jwtUtil.getAccessTokenExpirationMs());

            // Construction de la réponse
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getSiteId(),
                user.getLastLoginAt()
            );

            LoginResponse response = new LoginResponse(
                newAccessToken,
                refreshToken, // Réutilisation du même refresh token
                jwtUtil.getAccessTokenExpirationMs() / 1000,
                userInfo
            );

            logger.info("Token rafraîchi avec succès pour l'utilisateur: {}", user.getEmail());
            return response;

        } catch (Exception e) {
            logger.error("Erreur lors du rafraîchissement du token", e);
            throw new AuthenticationException("Impossible de rafraîchir le token");
        }
    }

    /**
     * Déconnecte l'utilisateur actuel
     */
    public void logout() {
        SecurityContextHolder.clearContext();
        logger.info("Utilisateur déconnecté");
    }

    /**
     * Obtient l'utilisateur actuellement connecté
     */
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthenticationException("Aucun utilisateur connecté");
        }

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        return userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new AuthenticationException("Utilisateur non trouvé"));
    }
}

