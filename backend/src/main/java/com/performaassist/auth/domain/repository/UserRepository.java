package com.performaassist.auth.domain.repository;

import com.performaassist.auth.domain.model.User;
import com.performaassist.auth.domain.model.UserRole;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) pour l'accès aux données des utilisateurs
 * 
 * Cette interface définit les opérations de persistance pour les utilisateurs
 * selon les principes de l'architecture hexagonale. L'implémentation concrète
 * sera fournie par la couche infrastructure.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public interface UserRepository {

    /**
     * Recherche un utilisateur par son email
     * 
     * @param email l'adresse email de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur par son ID
     * 
     * @param id l'identifiant unique de l'utilisateur
     * @return Optional contenant l'utilisateur s'il existe
     */
    Optional<User> findById(UUID id);

    /**
     * Recherche tous les utilisateurs actifs
     * 
     * @return liste des utilisateurs actifs
     */
    List<User> findAllActive();

    /**
     * Recherche les utilisateurs par rôle
     * 
     * @param role le rôle recherché
     * @return liste des utilisateurs ayant ce rôle
     */
    List<User> findByRole(UserRole role);

    /**
     * Recherche les utilisateurs d'un site spécifique
     * 
     * @param siteId l'identifiant du site
     * @return liste des utilisateurs du site
     */
    List<User> findBySiteId(UUID siteId);

    /**
     * Vérifie si un email existe déjà
     * 
     * @param email l'adresse email à vérifier
     * @return true si l'email existe, false sinon
     */
    boolean existsByEmail(String email);

    /**
     * Sauvegarde un utilisateur
     * 
     * @param user l'utilisateur à sauvegarder
     * @return l'utilisateur sauvegardé avec son ID généré
     */
    User save(User user);

    /**
     * Supprime un utilisateur (soft delete - désactivation)
     * 
     * @param id l'identifiant de l'utilisateur à supprimer
     */
    void deleteById(UUID id);

    /**
     * Compte le nombre total d'utilisateurs actifs
     * 
     * @return le nombre d'utilisateurs actifs
     */
    long countActive();

    /**
     * Recherche les utilisateurs par nom ou prénom (recherche partielle)
     * 
     * @param searchTerm le terme de recherche
     * @return liste des utilisateurs correspondants
     */
    List<User> findByNameContaining(String searchTerm);

    /**
     * Met à jour la date de dernière connexion
     * 
     * @param userId l'identifiant de l'utilisateur
     */
    void updateLastLoginAt(UUID userId);
}

