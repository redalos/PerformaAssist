package com.performaassist.checklist.application.usecase;

import com.performaassist.auth.application.usecase.AuthenticationService;
import com.performaassist.auth.domain.model.User;
import com.performaassist.checklist.application.dto.ChecklistTemplateDto;
import com.performaassist.checklist.domain.model.ChecklistItem;
import com.performaassist.checklist.domain.model.ChecklistTemplate;
import com.performaassist.checklist.domain.repository.ChecklistTemplateRepository;
import com.performaassist.shared.exception.BusinessException;
import com.performaassist.shared.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Service pour la gestion des modèles de checklist
 * 
 * Ce service gère les cas d'usage liés aux modèles de checklist :
 * - Création et modification de modèles
 * - Gestion des éléments de checklist
 * - Recherche et filtrage
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Service
@Transactional
public class ChecklistTemplateService {

    private static final Logger logger = LoggerFactory.getLogger(ChecklistTemplateService.class);

    private final ChecklistTemplateRepository templateRepository;
    private final AuthenticationService authenticationService;

    @Autowired
    public ChecklistTemplateService(ChecklistTemplateRepository templateRepository,
                                  AuthenticationService authenticationService) {
        this.templateRepository = templateRepository;
        this.authenticationService = authenticationService;
    }

    /**
     * Crée un nouveau modèle de checklist
     */
    public ChecklistTemplateDto createTemplate(ChecklistTemplateDto templateDto) {
        User currentUser = authenticationService.getCurrentUser();
        
        // Vérifier que le nom n'existe pas déjà
        if (templateRepository.existsByName(templateDto.getName())) {
            throw new BusinessException("Un modèle avec ce nom existe déjà");
        }

        // Créer l'entité
        ChecklistTemplate template = new ChecklistTemplate(
            templateDto.getName(),
            templateDto.getDescription(),
            templateDto.getCategory(),
            currentUser.getId()
        );

        // Ajouter les éléments
        if (templateDto.getItems() != null) {
            for (ChecklistTemplateDto.ChecklistItemDto itemDto : templateDto.getItems()) {
                ChecklistItem item = new ChecklistItem(
                    itemDto.getTitle(),
                    itemDto.getDescription(),
                    itemDto.getOrderIndex(),
                    itemDto.getIsMandatory(),
                    itemDto.getScoringType(),
                    itemDto.getMaxScore()
                );
                template.addItem(item);
            }
        }

        // Sauvegarder
        ChecklistTemplate savedTemplate = templateRepository.save(template);
        
        logger.info("Modèle de checklist créé: {} par l'utilisateur: {}", 
                   savedTemplate.getName(), currentUser.getEmail());

        return convertToDto(savedTemplate);
    }

    /**
     * Met à jour un modèle de checklist existant
     */
    public ChecklistTemplateDto updateTemplate(UUID id, ChecklistTemplateDto templateDto) {
        User currentUser = authenticationService.getCurrentUser();
        
        ChecklistTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modèle de checklist non trouvé"));

        // Vérifier que le nom n'existe pas déjà (en excluant le modèle actuel)
        if (templateRepository.existsByNameAndIdNot(templateDto.getName(), id)) {
            throw new BusinessException("Un modèle avec ce nom existe déjà");
        }

        // Mettre à jour les propriétés
        template.setName(templateDto.getName());
        template.setDescription(templateDto.getDescription());
        template.setCategory(templateDto.getCategory());
        template.setUpdatedBy(currentUser.getId());

        // Mettre à jour les éléments (suppression et recréation pour simplifier)
        template.getItems().clear();
        
        if (templateDto.getItems() != null) {
            for (ChecklistTemplateDto.ChecklistItemDto itemDto : templateDto.getItems()) {
                ChecklistItem item = new ChecklistItem(
                    itemDto.getTitle(),
                    itemDto.getDescription(),
                    itemDto.getOrderIndex(),
                    itemDto.getIsMandatory(),
                    itemDto.getScoringType(),
                    itemDto.getMaxScore()
                );
                template.addItem(item);
            }
        }

        ChecklistTemplate savedTemplate = templateRepository.save(template);
        
        logger.info("Modèle de checklist mis à jour: {} par l'utilisateur: {}", 
                   savedTemplate.getName(), currentUser.getEmail());

        return convertToDto(savedTemplate);
    }

    /**
     * Récupère un modèle de checklist par son ID
     */
    @Transactional(readOnly = true)
    public ChecklistTemplateDto getTemplateById(UUID id) {
        ChecklistTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modèle de checklist non trouvé"));

        return convertToDto(template);
    }

    /**
     * Récupère tous les modèles actifs
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> getAllActiveTemplates() {
        List<ChecklistTemplate> templates = templateRepository.findAllActive();
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche les modèles par catégorie
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> getTemplatesByCategory(String category) {
        List<ChecklistTemplate> templates = templateRepository.findByCategory(category);
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche les modèles créés par l'utilisateur actuel
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> getMyTemplates() {
        User currentUser = authenticationService.getCurrentUser();
        List<ChecklistTemplate> templates = templateRepository.findByCreatedBy(currentUser.getId());
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Recherche les modèles par nom
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> searchTemplatesByName(String name) {
        List<ChecklistTemplate> templates = templateRepository.findByNameContaining(name);
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Supprime un modèle de checklist (soft delete)
     */
    public void deleteTemplate(UUID id) {
        User currentUser = authenticationService.getCurrentUser();
        
        ChecklistTemplate template = templateRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Modèle de checklist non trouvé"));

        // Vérifier les permissions (seul le créateur ou un admin peut supprimer)
        if (!template.getCreatedBy().equals(currentUser.getId()) && !currentUser.isAdmin()) {
            throw new BusinessException("Vous n'avez pas l'autorisation de supprimer ce modèle");
        }

        templateRepository.deleteById(id);
        
        logger.info("Modèle de checklist supprimé: {} par l'utilisateur: {}", 
                   template.getName(), currentUser.getEmail());
    }

    /**
     * Récupère les catégories disponibles
     */
    @Transactional(readOnly = true)
    public List<String> getAvailableCategories() {
        return templateRepository.findDistinctCategories();
    }

    /**
     * Récupère les modèles les plus récents
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplateDto> getRecentTemplates(int limit) {
        List<ChecklistTemplate> templates = templateRepository.findRecentTemplates(limit);
        return templates.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    /**
     * Convertit une entité en DTO
     */
    private ChecklistTemplateDto convertToDto(ChecklistTemplate template) {
        ChecklistTemplateDto dto = new ChecklistTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setDescription(template.getDescription());
        dto.setCategory(template.getCategory());
        dto.setCreatedBy(template.getCreatedBy());
        dto.setIsActive(template.getIsActive());
        dto.setCreatedAt(template.getCreatedAt());
        dto.setUpdatedAt(template.getUpdatedAt());
        dto.setUpdatedBy(template.getUpdatedBy());

        // Convertir les éléments
        List<ChecklistTemplateDto.ChecklistItemDto> itemDtos = template.getItems().stream()
                .map(this::convertItemToDto)
                .collect(Collectors.toList());
        dto.setItems(itemDtos);

        return dto;
    }

    /**
     * Convertit un élément en DTO
     */
    private ChecklistTemplateDto.ChecklistItemDto convertItemToDto(ChecklistItem item) {
        ChecklistTemplateDto.ChecklistItemDto dto = new ChecklistTemplateDto.ChecklistItemDto();
        dto.setId(item.getId());
        dto.setOrderIndex(item.getOrderIndex());
        dto.setTitle(item.getTitle());
        dto.setDescription(item.getDescription());
        dto.setIsMandatory(item.getIsMandatory());
        dto.setScoringType(item.getScoringType());
        dto.setMaxScore(item.getMaxScore());
        return dto;
    }
}

