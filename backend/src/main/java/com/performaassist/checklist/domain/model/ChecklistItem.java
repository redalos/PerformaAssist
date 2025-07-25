package com.performaassist.checklist.domain.model;

import jakarta.persistence.*;

import java.util.UUID;

/**
 * Entité représentant un élément d'une checklist
 * 
 * Chaque élément définit un point de contrôle spécifique
 * avec ses critères d'évaluation et son système de notation.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Entity
@Table(name = "checklist_items")
public class ChecklistItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private ChecklistTemplate template;

    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "is_mandatory", nullable = false)
    private Boolean isMandatory = true;

    @Enumerated(EnumType.STRING)
    @Column(name = "scoring_type", nullable = false, length = 20)
    private ScoringType scoringType = ScoringType.BINARY;

    @Column(name = "max_score", nullable = false)
    private Integer maxScore = 1;

    // Constructeurs
    public ChecklistItem() {}

    public ChecklistItem(String title, String description, Integer orderIndex, Boolean isMandatory) {
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
        this.isMandatory = isMandatory;
        this.scoringType = ScoringType.BINARY;
        this.maxScore = 1;
    }

    public ChecklistItem(String title, String description, Integer orderIndex, 
                        Boolean isMandatory, ScoringType scoringType, Integer maxScore) {
        this.title = title;
        this.description = description;
        this.orderIndex = orderIndex;
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

    public ChecklistTemplate getTemplate() {
        return template;
    }

    public void setTemplate(ChecklistTemplate template) {
        this.template = template;
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

    // Méthodes utilitaires
    public boolean isOptional() {
        return !isMandatory;
    }

    public boolean isBinaryScoring() {
        return ScoringType.BINARY.equals(scoringType);
    }

    public boolean isScaleScoring() {
        return ScoringType.SCALE.equals(scoringType);
    }

    public boolean isPercentageScoring() {
        return ScoringType.PERCENTAGE.equals(scoringType);
    }

    @Override
    public String toString() {
        return "ChecklistItem{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", orderIndex=" + orderIndex +
                ", isMandatory=" + isMandatory +
                ", scoringType=" + scoringType +
                ", maxScore=" + maxScore +
                '}';
    }
}

