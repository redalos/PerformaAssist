package com.performaassist.auth.infrastructure.persistence;

import com.performaassist.auth.domain.model.User;
import com.performaassist.auth.domain.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur JPA pour l'accès aux données des utilisateurs
 * 
 * Cette interface étend JpaRepository et fournit l'implémentation concrète
 * du port UserRepository en utilisant Spring Data JPA.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Repository
public interface JpaUserRepository extends JpaRepository<User, UUID> {

    /**
     * Recherche un utilisateur par son email
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche tous les utilisateurs actifs
     */
    List<User> findByIsActiveTrue();

    /**
     * Recherche les utilisateurs par rôle et statut actif
     */
    List<User> findByRoleAndIsActiveTrue(UserRole role);

    /**
     * Recherche les utilisateurs d'un site spécifique
     */
    List<User> findBySiteIdAndIsActiveTrue(UUID siteId);

    /**
     * Vérifie si un email existe déjà
     */
    boolean existsByEmail(String email);

    /**
     * Compte le nombre d'utilisateurs actifs
     */
    long countByIsActiveTrue();

    /**
     * Recherche par nom ou prénom (insensible à la casse)
     */
    @Query("SELECT u FROM User u WHERE u.isActive = true AND " +
           "(LOWER(u.firstName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(u.lastName) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
    List<User> findByNameContaining(@Param("searchTerm") String searchTerm);

    /**
     * Met à jour la date de dernière connexion
     */
    @Modifying
    @Query("UPDATE User u SET u.lastLoginAt = :loginTime WHERE u.id = :userId")
    void updateLastLoginAt(@Param("userId") UUID userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * Désactive un utilisateur (soft delete)
     */
    @Modifying
    @Query("UPDATE User u SET u.isActive = false WHERE u.id = :userId")
    void softDeleteById(@Param("userId") UUID userId);

    /**
     * Recherche les utilisateurs créés dans une période donnée
     */
    @Query("SELECT u FROM User u WHERE u.createdAt BETWEEN :startDate AND :endDate")
    List<User> findByCreatedAtBetween(@Param("startDate") LocalDateTime startDate, 
                                     @Param("endDate") LocalDateTime endDate);

    /**
     * Recherche les utilisateurs par rôle avec pagination
     */
    List<User> findByRoleAndIsActiveTrueOrderByLastNameAsc(UserRole role);

    /**
     * Recherche les utilisateurs d'un site avec un rôle spécifique
     */
    List<User> findBySiteIdAndRoleAndIsActiveTrue(UUID siteId, UserRole role);
}

