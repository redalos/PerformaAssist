package com.performaassist.checklist.infrastructure.web;

import com.performaassist.checklist.application.dto.ChecklistTemplateDto;
import com.performaassist.checklist.application.usecase.ChecklistTemplateService;
import com.performaassist.shared.exception.BusinessException;
import com.performaassist.shared.exception.ResourceNotFoundException;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour les modèles de checklist
 * 
 * Ce contrôleur expose les endpoints pour la gestion des modèles de checklist :
 * - CRUD des modèles
 * - Recherche et filtrage
 * - Gestion des catégories
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@RestController
@RequestMapping("/checklists/templates")
@Tag(name = "Modèles de Checklist", description = "API de gestion des modèles de checklist")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ChecklistTemplateController {

    private static final Logger logger = LoggerFactory.getLogger(ChecklistTemplateController.class);

    private final ChecklistTemplateService templateService;

    @Autowired
    public ChecklistTemplateController(ChecklistTemplateService templateService) {
        this.templateService = templateService;
    }

    /**
     * Crée un nouveau modèle de checklist
     */
    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Créer un modèle de checklist", description = "Crée un nouveau modèle de checklist avec ses éléments")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Modèle créé avec succès"),
        @ApiResponse(responseCode = "400", description = "Données invalides"),
        @ApiResponse(responseCode = "409", description = "Nom de modèle déjà existant")
    })
    public ResponseEntity<?> createTemplate(@Valid @RequestBody ChecklistTemplateDto templateDto) {
        try {
            ChecklistTemplateDto createdTemplate = templateService.createTemplate(templateDto);
            logger.info("Modèle de checklist créé avec succès: {}", createdTemplate.getName());
            return ResponseEntity.status(HttpStatus.CREATED).body(createdTemplate);
            
        } catch (BusinessException e) {
            logger.warn("Erreur métier lors de la création du modèle: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de validation");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la création du modèle", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Met à jour un modèle de checklist existant
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Mettre à jour un modèle", description = "Met à jour un modèle de checklist existant")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modèle mis à jour avec succès"),
        @ApiResponse(responseCode = "404", description = "Modèle non trouvé"),
        @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<?> updateTemplate(@PathVariable UUID id, @Valid @RequestBody ChecklistTemplateDto templateDto) {
        try {
            ChecklistTemplateDto updatedTemplate = templateService.updateTemplate(id, templateDto);
            logger.info("Modèle de checklist mis à jour avec succès: {}", updatedTemplate.getName());
            return ResponseEntity.ok(updatedTemplate);
            
        } catch (ResourceNotFoundException e) {
            logger.warn("Modèle non trouvé pour mise à jour: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ressource non trouvée");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            
        } catch (BusinessException e) {
            logger.warn("Erreur métier lors de la mise à jour du modèle: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur de validation");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
            
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la mise à jour du modèle", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère un modèle de checklist par son ID
     */
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un modèle", description = "Récupère un modèle de checklist par son ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modèle récupéré avec succès"),
        @ApiResponse(responseCode = "404", description = "Modèle non trouvé")
    })
    public ResponseEntity<?> getTemplate(@PathVariable UUID id) {
        try {
            ChecklistTemplateDto template = templateService.getTemplateById(id);
            return ResponseEntity.ok(template);
            
        } catch (ResourceNotFoundException e) {
            logger.warn("Modèle non trouvé: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ressource non trouvée");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du modèle: {}", id, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère tous les modèles actifs
     */
    @GetMapping
    @Operation(summary = "Lister les modèles", description = "Récupère tous les modèles de checklist actifs")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    public ResponseEntity<?> getAllTemplates(@RequestParam(required = false) String category,
                                           @RequestParam(required = false) String search) {
        try {
            List<ChecklistTemplateDto> templates;
            
            if (category != null && !category.trim().isEmpty()) {
                templates = templateService.getTemplatesByCategory(category);
            } else if (search != null && !search.trim().isEmpty()) {
                templates = templateService.searchTemplatesByName(search);
            } else {
                templates = templateService.getAllActiveTemplates();
            }
            
            return ResponseEntity.ok(templates);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des modèles", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère les modèles créés par l'utilisateur actuel
     */
    @GetMapping("/my-templates")
    @Operation(summary = "Mes modèles", description = "Récupère les modèles créés par l'utilisateur connecté")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Liste récupérée avec succès")
    })
    public ResponseEntity<?> getMyTemplates() {
        try {
            List<ChecklistTemplateDto> templates = templateService.getMyTemplates();
            return ResponseEntity.ok(templates);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des modèles utilisateur", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Supprime un modèle de checklist
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
    @Operation(summary = "Supprimer un modèle", description = "Supprime un modèle de checklist (soft delete)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Modèle supprimé avec succès"),
        @ApiResponse(responseCode = "404", description = "Modèle non trouvé"),
        @ApiResponse(responseCode = "403", description = "Accès refusé")
    })
    public ResponseEntity<?> deleteTemplate(@PathVariable UUID id) {
        try {
            templateService.deleteTemplate(id);
            logger.info("Modèle de checklist supprimé avec succès: {}", id);
            return ResponseEntity.noContent().build();
            
        } catch (ResourceNotFoundException e) {
            logger.warn("Modèle non trouvé pour suppression: {}", id);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Ressource non trouvée");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
            
        } catch (BusinessException e) {
            logger.warn("Erreur d'autorisation lors de la suppression: {}", e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", "Accès refusé");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(error);
            
        } catch (Exception e) {
            logger.error("Erreur inattendue lors de la suppression du modèle: {}", id, e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère les catégories disponibles
     */
    @GetMapping("/categories")
    @Operation(summary = "Lister les catégories", description = "Récupère toutes les catégories de modèles disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Catégories récupérées avec succès")
    })
    public ResponseEntity<?> getCategories() {
        try {
            List<String> categories = templateService.getAvailableCategories();
            return ResponseEntity.ok(categories);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Récupère les modèles les plus récents
     */
    @GetMapping("/recent")
    @Operation(summary = "Modèles récents", description = "Récupère les modèles les plus récemment créés")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Modèles récents récupérés avec succès")
    })
    public ResponseEntity<?> getRecentTemplates(@RequestParam(defaultValue = "10") int limit) {
        try {
            List<ChecklistTemplateDto> templates = templateService.getRecentTemplates(limit);
            return ResponseEntity.ok(templates);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des modèles récents", e);
            Map<String, String> error = new HashMap<>();
            error.put("error", "Erreur interne");
            error.put("message", "Une erreur inattendue s'est produite");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

