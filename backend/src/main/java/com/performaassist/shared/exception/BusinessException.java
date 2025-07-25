package com.performaassist.shared.exception;

/**
 * Exception personnalisée pour les erreurs métier
 * 
 * Cette exception est levée lors d'erreurs liées à la logique métier
 * de l'application PerformaAssist.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

