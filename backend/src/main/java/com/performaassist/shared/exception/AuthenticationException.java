package com.performaassist.shared.exception;

/**
 * Exception personnalisée pour les erreurs d'authentification
 * 
 * Cette exception est levée lors d'erreurs liées à l'authentification
 * et l'autorisation dans l'application PerformaAssist.
 * 
 * @author PerformaAssist Team
 * @version 1.0.0
 */
public class AuthenticationException extends RuntimeException {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}

