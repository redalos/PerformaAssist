package com.performaassist.checklist.domain.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Entité représentant un modèle de checklist
 * 
 * Un modèle de checklist définit la structure et les éléments
 * qui seront utilisés lors des exécutions de checklists terrain.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Entity
@Table(name = "checklist_templates")
@EntityListeners(AuditingEntityListener.class)
public class ChecklistTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "category", length = 100)
    private String category;

    @Column(name = "created_by", nullable = false)
    private UUID createdBy;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "updated_by")
    private UUID updatedBy;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex ASC")
    private List<ChecklistItem> items = new ArrayList<>();

    // Constructeurs
    public ChecklistTemplate() {}

    public ChecklistTemplate(String name, String description, String category, UUID createdBy) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.createdBy = createdBy;
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

    public List<ChecklistItem> getItems() {
        return items;
    }

    public void setItems(List<ChecklistItem> items) {
        this.items = items;
    }

    // Méthodes utilitaires
    public void addItem(ChecklistItem item) {
        items.add(item);
        item.setTemplate(this);
    }

    public void removeItem(ChecklistItem item) {
        items.remove(item);
        item.setTemplate(null);
    }

    public int getItemCount() {
        return items.size();
    }

    public int getMandatoryItemCount() {
        return (int) items.stream().filter(ChecklistItem::getIsMandatory).count();
    }

    public int getMaxPossibleScore() {
        return items.stream().mapToInt(ChecklistItem::getMaxScore).sum();
    }

    public void activate() {
        this.isActive = true;
    }

    public void deactivate() {
        this.isActive = false;
    }

    @Override
    public String toString() {
        return "ChecklistTemplate{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", itemCount=" + getItemCount() +
                ", isActive=" + isActive +
                '}';
    }
}

