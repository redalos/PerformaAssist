package com.performaassist.auth.infrastructure.web;

import com.performaassist.auth.application.dto.LoginRequest;
import com.performaassist.auth.application.dto.LoginResponse;
import com.performaassist.auth.application.usecase.AuthenticationService;
import com.performaassist.auth.domain.model.User;
import com.performaassist.shared.exception.AuthenticationException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour l'authentification
 * 
 * Ce contrôleur expose les endpoints pour l'authentification des utilisateurs :
 * - Connexion
 * - Rafraîchissement des tokens
 * - Déconnexion
 * - Profil utilisateur
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentification", description = "API d'authentification et gestion des sessions")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final AuthenticationService authenticationService;

    @Autowired
    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    /**
     * Endpoint de connexion utilisateur
     */
    @PostMapping("/login")
    @Operation(summary = "Connexion utilisateur", description = "Authentifie un utilisateur et retourne les tokens JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Connexion réussie"),
        @ApiResponse(responseCode = "401", description = "Identifiants invalides"),
        @ApiResponse(responseCode = "400", description = "Données de requête invalides")
    })
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            LoginResponse response = authenticationService.authenticateUser(loginRequest);
            
            logger.info("Connexion réussie pour l'utilisateur: {}", loginRequest.getEmail());
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            logger.warn("Tentative de connexion échouée pour l'utilisateur: {}", loginRequest.getEmail());
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Authentification échouée");
            error.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la connexion", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint de rafraîchissement des tokens
     */
    @PostMapping("/refresh")
    @Operation(summary = "Rafraîchissement du token", description = "Génère un nouveau token d'accès à partir du token de rafraîchissement")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Token rafraîchi avec succès"),
        @ApiResponse(responseCode = "401", description = "Token de rafraîchissement invalide")
    })
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        try {
            String refreshToken = request.get("refreshToken");
            if (refreshToken == null || refreshToken.trim().isEmpty()) {
                Map<String, String> error = new HashMap<>();
                error.put("error", "Token manquant");
                error.put("message", "Le token de rafraîchissement est requis");
                return ResponseEntity.badRequest().body(error);
            }

            LoginResponse response = authenticationService.refreshToken(refreshToken);
            return ResponseEntity.ok(response);
            
        } catch (AuthenticationException e) {
            logger.warn("Échec du rafraîchissement du token: {}", e.getMessage());
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Token invalide");
            error.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Erreur lors du rafraîchissement du token", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Impossible de rafraîchir le token");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint de déconnexion
     */
    @PostMapping("/logout")
    @Operation(summary = "Déconnexion", description = "Déconnecte l'utilisateur actuel")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Déconnexion réussie")
    })
    public ResponseEntity<?> logout() {
        try {
            authenticationService.logout();
            
            Map<String, String> response = new HashMap<>();
            response.put("message", "Déconnexion réussie");
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la déconnexion", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de déconnexion");
            error.put("message", "Une erreur s'est produite lors de la déconnexion");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Endpoint pour obtenir le profil de l'utilisateur connecté
     */
    @GetMapping("/profile")
    @Operation(summary = "Profil utilisateur", description = "Retourne les informations de l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Profil récupéré avec succès"),
        @ApiResponse(responseCode = "401", description = "Utilisateur non authentifié")
    })
    public ResponseEntity<?> getCurrentUser() {
        try {
            User user = authenticationService.getCurrentUser();
            
            LoginResponse.UserInfo userInfo = new LoginResponse.UserInfo(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                user.getSiteId(),
                user.getLastLoginAt()
            );
            
            return ResponseEntity.ok(userInfo);
            
        } catch (AuthenticationException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Non authentifié");
            error.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du profil utilisateur", e);
            
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Impossible de récupérer le profil utilisateur");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

