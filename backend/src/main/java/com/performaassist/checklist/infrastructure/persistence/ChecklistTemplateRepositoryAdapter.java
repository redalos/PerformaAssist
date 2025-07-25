package com.performaassist.checklist.infrastructure.persistence;

import com.performaassist.checklist.domain.model.ChecklistTemplate;
import com.performaassist.checklist.domain.repository.ChecklistTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port ChecklistTemplateRepository
 * 
 * Cette classe fait le pont entre la couche domaine et la couche infrastructure
 * en adaptant l'interface JPA aux besoins du domaine métier.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Component
@Transactional
public class ChecklistTemplateRepositoryAdapter implements ChecklistTemplateRepository {

    private final JpaChecklistTemplateRepository jpaRepository;

    @Autowired
    public ChecklistTemplateRepositoryAdapter(JpaChecklistTemplateRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ChecklistTemplate> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findAllActive() {
        return jpaRepository.findByIsActiveTrueOrderByCreatedAtDesc();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findByCategory(String category) {
        return jpaRepository.findByCategoryAndIsActiveTrueOrderByNameAsc(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findByCreatedBy(UUID createdBy) {
        return jpaRepository.findByCreatedByAndIsActiveTrueOrderByCreatedAtDesc(createdBy);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findByNameContaining(String name) {
        return jpaRepository.findByNameContaining(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return jpaRepository.existsByNameAndIsActiveTrue(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByNameAndIdNot(String name, UUID excludeId) {
        return jpaRepository.existsByNameAndIsActiveTrueAndIdNot(name, excludeId);
    }

    @Override
    public ChecklistTemplate save(ChecklistTemplate template) {
        return jpaRepository.save(template);
    }

    @Override
    public void deleteById(UUID id) {
        jpaRepository.softDeleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return jpaRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> findDistinctCategories() {
        return jpaRepository.findDistinctCategoriesByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findRecentTemplates(int limit) {
        return jpaRepository.findRecentTemplates(limit);
    }

    /**
     * Méthodes supplémentaires pour optimiser les requêtes avec les éléments
     */
    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findAllActiveWithItems() {
        return jpaRepository.findAllActiveWithItems();
    }

    @Transactional(readOnly = true)
    public Optional<ChecklistTemplate> findByIdWithItems(UUID id) {
        ChecklistTemplate template = jpaRepository.findByIdWithItems(id);
        return Optional.ofNullable(template);
    }

    @Transactional(readOnly = true)
    public List<ChecklistTemplate> findByCreatedByWithItems(UUID createdBy) {
        return jpaRepository.findByCreatedByWithItems(createdBy);
    }
}

