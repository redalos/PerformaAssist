package com.performaassist.auth.infrastructure.persistence;

import com.performaassist.auth.domain.model.User;
import com.performaassist.auth.domain.model.UserRole;
import com.performaassist.auth.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Adaptateur qui implémente le port UserRepository
 * 
 * Cette classe fait le pont entre la couche domaine et la couche infrastructure
 * en adaptant l'interface JPA aux besoins du domaine métier.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Component
@Transactional
public class UserRepositoryAdapter implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Autowired
    public UserRepositoryAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(UUID id) {
        return jpaUserRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findAllActive() {
        return jpaUserRepository.findByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByRole(UserRole role) {
        return jpaUserRepository.findByRoleAndIsActiveTrue(role);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findBySiteId(UUID siteId) {
        return jpaUserRepository.findBySiteIdAndIsActiveTrue(siteId);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    @Override
    public User save(User user) {
        return jpaUserRepository.save(user);
    }

    @Override
    public void deleteById(UUID id) {
        jpaUserRepository.softDeleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public long countActive() {
        return jpaUserRepository.countByIsActiveTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> findByNameContaining(String searchTerm) {
        return jpaUserRepository.findByNameContaining(searchTerm);
    }

    @Override
    public void updateLastLoginAt(UUID userId) {
        jpaUserRepository.updateLastLoginAt(userId, LocalDateTime.now());
    }
}

