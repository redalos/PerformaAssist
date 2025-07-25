package com.performaassist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Application principale PerformaAssist
 * 
 * Cette classe démarre l'application Spring Boot avec toutes les configurations nécessaires :
 * - JPA Auditing pour la traçabilité automatique des entités
 * - Cache pour optimiser les performances
 * - Traitement asynchrone pour les tâches longues
 * - Planification pour les tâches récurrentes
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
@SpringBootApplication
@EnableJpaAuditing
@EnableCaching
@EnableAsync
@EnableScheduling
public class PerformaAssistApplication {

    public static void main(String[] args) {
        SpringApplication.run(PerformaAssistApplication.class, args);
    }
}

