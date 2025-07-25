package com.performaassist.checklist.application.dto;

import com.performaassist.checklist.domain.model.ScoringType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * DTO pour les modèles de checklist
 * 
 * Cette classe représente les données d'un modèle de checklist
 * échangées entre le frontend et le backend.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public class ChecklistTemplateDto {

    private UUID id;

    @NotBlank(message = "Le nom est requis")
    @Size(max = 255, message = "Le nom ne peut pas dépasser 255 caractères")
    private String name;

    @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
    private String description;

    @Size(max = 100, message = "La catégorie ne peut pas dépasser 100 caractères")
    private String category;

    private UUID createdBy;
    private String createdByName;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UUID updatedBy;
    private String updatedByName;

    @Valid
    private List<ChecklistItemDto> items = new ArrayList<>();

    // Constructeurs
    public ChecklistTemplateDto() {}

    public ChecklistTemplateDto(String name, String description, String category) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.isActive = true;
    }

    // Getters et Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public UUID getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UUID createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public UUID getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(UUID updatedBy) {
        this.updatedBy = updatedBy;
    }

    public String getUpdatedByName() {
        return updatedByName;
    }

    public void setUpdatedByName(String updatedByName) {
        this.updatedByName = updatedByName;
    }

    public List<ChecklistItemDto> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItemDto> items) {
        this.items = items;
    }

    // Méthodes utilitaires
    public void addItem(ChecklistItemDto item) {
        this.items.add(item);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getMandatoryItemCount() {
        return (int) items.stream().filter(ChecklistItemDto::getIsMandatory).count();
    }

    public int getMaxPossibleScore() {
        return items.stream().mapToInt(ChecklistItemDto::getMaxScore).sum();
    }

    /**
     * Classe interne pour les éléments de checklist
     */
    public static class ChecklistItemDto {
        private UUID id;

        @NotNull(message = "L'index d'ordre est requis")
        private Integer orderIndex;

        @NotBlank(message = "Le titre est requis")
        @Size(max = 255, message = "Le titre ne peut pas dépasser 255 caractères")
        private String title;

        @Size(max = 1000, message = "La description ne peut pas dépasser 1000 caractères")
        private String description;

        @NotNull(message = "Le caractère obligatoire doit être spécifié")
        private Boolean isMandatory = true;

        @NotNull(message = "Le type de notation est requis")
        private ScoringType scoringType = ScoringType.BINARY;

        @NotNull(message = "Le score maximum est requis")
        private Integer maxScore = 1;

        // Constructeurs
        public ChecklistItemDto() {}

        public ChecklistItemDto(Integer orderIndex, String title, String description, 
                               Boolean isMandatory, ScoringType scoringType, Integer maxScore) {
            this.orderIndex = orderIndex;
            this.title = title;
            this.description = description;
            this.isMandatory = isMandatory;
            this.scoringType = scoringType;
            this.maxScore = maxScore;
        }

        // Getters et Setters
        public UUID getId() {
            return id;
        }

        public void setId(UUID id) {
            this.id = id;
        }

        public Integer getOrderIndex() {
            return orderIndex;
        }

        public void setOrderIndex(Integer orderIndex) {
            this.orderIndex = orderIndex;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Boolean getIsMandatory() {
            return isMandatory;
        }

        public void setIsMandatory(Boolean isMandatory) {
            this.isMandatory = isMandatory;
        }

        public ScoringType getScoringType() {
            return scoringType;
        }

        public void setScoringType(ScoringType scoringType) {
            this.scoringType = scoringType;
        }

        public Integer getMaxScore() {
            return maxScore;
        }

        public void setMaxScore(Integer maxScore) {
            this.maxScore = maxScore;
        }
    }
}

