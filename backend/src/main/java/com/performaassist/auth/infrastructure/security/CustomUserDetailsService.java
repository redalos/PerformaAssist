package com.performaassist.auth.infrastructure.security;

import com.performaassist.auth.domain.model.User;
import com.performaassist.auth.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Service personnalisé pour charger les détails utilisateur
 * 
 * Cette classe implémente UserDetailsService de Spring Security
 * pour charger les informations utilisateur depuis la base de données.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Charge un utilisateur par son email (username)
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Utilisateur non trouvé avec l'email : " + email));

        return UserPrincipal.create(user);
    }

    /**
     * Charge un utilisateur par son ID
     * Utilisé pour la validation des tokens JWT
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException(
                    "Utilisateur non trouvé avec l'ID : " + id));

        return UserPrincipal.create(user);
    }
}

