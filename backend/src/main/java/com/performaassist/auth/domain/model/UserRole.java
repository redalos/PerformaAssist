package com.performaassist.auth.domain.model;

/**
 * Énumération des rôles utilisateur dans PerformaAssist
 * 
 * Cette énumération définit les différents niveaux d'autorisation :
 * - ADMIN : Accès complet à toutes les fonctionnalités
 * - MANAGER : Accès aux fonctionnalités de gestion d'équipe
 * - EMPLOYEE : Accès aux fonctionnalités terrain
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public enum UserRole {
    
    /**
     * Administrateur système
     * - Gestion complète des utilisateurs
     * - Configuration système
     * - Accès à toutes les données
     */
    ADMIN("Administrateur", "Accès complet au système"),
    
    /**
     * Manager d'équipe
     * - Création et gestion des checklists
     * - Suivi des plans d'action
     * - Animation des réunions
     * - Consultation des KPI de son équipe
     */
    MANAGER("Manager", "Gestion d'équipe et supervision"),
    
    /**
     * Employé terrain
     * - Exécution des checklists
     * - Mise à jour des actions assignées
     * - Consultation des documents
     * - Saisie des données KPI
     */
    EMPLOYEE("Employé", "Exécution des tâches terrain");

    private final String displayName;
    private final String description;

    UserRole(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Vérifie si ce rôle a un niveau d'autorisation supérieur ou égal au rôle donné
     */
    public boolean hasAuthorityLevel(UserRole requiredRole) {
        return this.ordinal() <= requiredRole.ordinal();
    }

    /**
     * Retourne le niveau d'autorisation numérique (plus bas = plus de privilèges)
     */
    public int getAuthorityLevel() {
        return this.ordinal();
    }

    /**
     * Vérifie si ce rôle peut gérer le rôle donné
     */
    public boolean canManage(UserRole targetRole) {
        return this.hasAuthorityLevel(targetRole) && this != targetRole;
    }

    @Override
    public String toString() {
        return displayName;
    }
}

