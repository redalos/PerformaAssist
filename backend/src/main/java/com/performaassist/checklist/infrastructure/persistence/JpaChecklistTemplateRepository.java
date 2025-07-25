package com.performaassist.checklist.infrastructure.persistence;

import com.performaassist.checklist.domain.model.ChecklistTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

/**
 * Adaptateur JPA pour l'accès aux données des modèles de checklist
 * 
 * Cette interface étend JpaRepository et fournit l'implémentation concrète
 * du port ChecklistTemplateRepository en utilisant Spring Data JPA.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Repository
public interface JpaChecklistTemplateRepository extends JpaRepository<ChecklistTemplate, UUID> {

    /**
     * Recherche tous les modèles actifs
     */
    List<ChecklistTemplate> findByIsActiveTrueOrderByCreatedAtDesc();

    /**
     * Recherche les modèles par catégorie et statut actif
     */
    List<ChecklistTemplate> findByCategoryAndIsActiveTrueOrderByNameAsc(String category);

    /**
     * Recherche les modèles créés par un utilisateur spécifique
     */
    List<ChecklistTemplate> findByCreatedByAndIsActiveTrueOrderByCreatedAtDesc(UUID createdBy);

    /**
     * Recherche par nom (insensible à la casse)
     */
    @Query("SELECT t FROM ChecklistTemplate t WHERE t.isActive = true AND " +
           "LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')) ORDER BY t.name ASC")
    List<ChecklistTemplate> findByNameContaining(@Param("name") String name);

    /**
     * Vérifie si un nom existe déjà
     */
    boolean existsByNameAndIsActiveTrue(String name);

    /**
     * Vérifie si un nom existe déjà (en excluant un ID spécifique)
     */
    boolean existsByNameAndIsActiveTrueAndIdNot(String name, UUID id);

    /**
     * Compte le nombre de modèles actifs
     */
    long countByIsActiveTrue();

    /**
     * Désactive un modèle (soft delete)
     */
    @Modifying
    @Query("UPDATE ChecklistTemplate t SET t.isActive = false WHERE t.id = :id")
    void softDeleteById(@Param("id") UUID id);

    /**
     * Recherche les catégories distinctes des modèles actifs
     */
    @Query("SELECT DISTINCT t.category FROM ChecklistTemplate t WHERE t.isActive = true AND t.category IS NOT NULL ORDER BY t.category")
    List<String> findDistinctCategoriesByIsActiveTrue();

    /**
     * Recherche les modèles les plus récents
     */
    @Query("SELECT t FROM ChecklistTemplate t WHERE t.isActive = true ORDER BY t.createdAt DESC")
    List<ChecklistTemplate> findRecentTemplates(@Param("limit") int limit);

    /**
     * Recherche les modèles avec leurs éléments
     */
    @Query("SELECT DISTINCT t FROM ChecklistTemplate t LEFT JOIN FETCH t.items WHERE t.isActive = true ORDER BY t.name")
    List<ChecklistTemplate> findAllActiveWithItems();

    /**
     * Recherche un modèle avec ses éléments par ID
     */
    @Query("SELECT t FROM ChecklistTemplate t LEFT JOIN FETCH t.items WHERE t.id = :id AND t.isActive = true")
    ChecklistTemplate findByIdWithItems(@Param("id") UUID id);

    /**
     * Recherche les modèles par créateur avec leurs éléments
     */
    @Query("SELECT DISTINCT t FROM ChecklistTemplate t LEFT JOIN FETCH t.items WHERE t.createdBy = :createdBy AND t.isActive = true ORDER BY t.createdAt DESC")
    List<ChecklistTemplate> findByCreatedByWithItems(@Param("createdBy") UUID createdBy);
}

