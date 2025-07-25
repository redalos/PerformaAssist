package com.performaassist.shared.exception;

/**
 * Exception personnalisée pour les ressources non trouvées
 * 
 * Cette exception est levée lorsqu'une ressource demandée
 * n'existe pas dans l'application PerformaAssist.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

