package com.performaassist.checklist.domain.model;

/**
 * Énumération des types de notation pour les éléments de checklist
 * 
 * Cette énumération définit les différents systèmes de notation
 * disponibles pour évaluer les éléments d'une checklist.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public enum ScoringType {
    
    /**
     * Notation binaire (Oui/Non, Conforme/Non conforme)
     * Score : 0 ou 1
     */
    BINARY("Binaire", "Notation Oui/Non ou Conforme/Non conforme", 1),
    
    /**
     * Notation sur échelle (ex: 1 à 5, 1 à 10)
     * Score : de 0 à la valeur maximale définie
     */
    SCALE("Échelle", "Notation sur une échelle de valeurs", 5),
    
    /**
     * Notation en pourcentage (0% à 100%)
     * Score : de 0 à 100
     */
    PERCENTAGE("Pourcentage", "Notation en pourcentage de 0% à 100%", 100);

    private final String displayName;
    private final String description;
    private final int defaultMaxScore;

    ScoringType(String displayName, String description, int defaultMaxScore) {
        this.displayName = displayName;
        this.description = description;
        this.defaultMaxScore = defaultMaxScore;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getDefaultMaxScore() {
        return defaultMaxScore;
    }

    /**
     * Valide si un score est valide pour ce type de notation
     */
    public boolean isValidScore(int score, int maxScore) {
        return score >= 0 && score <= maxScore;
    }

    /**
     * Calcule le pourcentage de réussite
     */
    public double calculatePercentage(int score, int maxScore) {
        if (maxScore == 0) return 0.0;
        return (double) score / maxScore * 100.0;
    }

    /**
     * Retourne les valeurs possibles pour ce type de notation
     */
    public String getPossibleValues(int maxScore) {
        switch (this) {
            case BINARY:
                return "0 (Non) ou 1 (Oui)";
            case SCALE:
                return "0 à " + maxScore;
            case PERCENTAGE:
                return "0% à 100%";
            default:
                return "0 à " + maxScore;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}

