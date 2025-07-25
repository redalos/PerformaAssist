package com.performaassist.checklist.domain.repository;

import com.performaassist.checklist.domain.model.ChecklistTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Port (interface) pour l'accès aux données des modèles de checklist
 * 
 * Cette interface définit les opérations de persistance pour les modèles
 * de checklist selon les principes de l'architecture hexagonale.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public interface ChecklistTemplateRepository {

    /**
     * Recherche un modèle de checklist par son ID
     * 
     * @param id l'identifiant unique du modèle
     * @return Optional contenant le modèle s'il existe
     */
    Optional<ChecklistTemplate> findById(UUID id);

    /**
     * Recherche tous les modèles actifs
     * 
     * @return liste des modèles actifs
     */
    List<ChecklistTemplate> findAllActive();

    /**
     * Recherche les modèles par catégorie
     * 
     * @param category la catégorie recherchée
     * @return liste des modèles de cette catégorie
     */
    List<ChecklistTemplate> findByCategory(String category);

    /**
     * Recherche les modèles créés par un utilisateur spécifique
     * 
     * @param createdBy l'identifiant de l'utilisateur créateur
     * @return liste des modèles créés par cet utilisateur
     */
    List<ChecklistTemplate> findByCreatedBy(UUID createdBy);

    /**
     * Recherche les modèles par nom (recherche partielle)
     * 
     * @param name le terme de recherche dans le nom
     * @return liste des modèles correspondants
     */
    List<ChecklistTemplate> findByNameContaining(String name);

    /**
     * Vérifie si un nom de modèle existe déjà
     * 
     * @param name le nom à vérifier
     * @return true si le nom existe, false sinon
     */
    boolean existsByName(String name);

    /**
     * Vérifie si un nom de modèle existe déjà (en excluant un ID spécifique)
     * 
     * @param name le nom à vérifier
     * @param excludeId l'ID à exclure de la vérification
     * @return true si le nom existe, false sinon
     */
    boolean existsByNameAndIdNot(String name, UUID excludeId);

    /**
     * Sauvegarde un modèle de checklist
     * 
     * @param template le modèle à sauvegarder
     * @return le modèle sauvegardé avec son ID généré
     */
    ChecklistTemplate save(ChecklistTemplate template);

    /**
     * Supprime un modèle de checklist (soft delete - désactivation)
     * 
     * @param id l'identifiant du modèle à supprimer
     */
    void deleteById(UUID id);

    /**
     * Compte le nombre total de modèles actifs
     * 
     * @return le nombre de modèles actifs
     */
    long countActive();

    /**
     * Recherche les catégories distinctes des modèles actifs
     * 
     * @return liste des catégories disponibles
     */
    List<String> findDistinctCategories();

    /**
     * Recherche les modèles les plus récents
     * 
     * @param limit le nombre maximum de résultats
     * @return liste des modèles les plus récents
     */
    List<ChecklistTemplate> findRecentTemplates(int limit);
}

