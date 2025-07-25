import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../services/auth.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService = inject(AuthService);
  const token = authService.getToken();

  // Ajouter le token d'authentification si disponible
  let authReq = req;
  if (token && !req.url.includes('/auth/login') && !req.url.includes('/auth/refresh')) {
    authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
  }

  return next(authReq).pipe(
    catchError(error => {
      // Si erreur 401 et qu'on a un refresh token, essayer de rafraîchir
      if (error.status === 401 && authService.getRefreshToken() && !req.url.includes('/auth/refresh')) {
        return authService.refreshToken().pipe(
          switchMap(() => {
            // Retry la requête originale avec le nouveau token
            const newToken = authService.getToken();
            const retryReq = req.clone({
              headers: req.headers.set('Authorization', `Bearer ${newToken}`)
            });
            return next(retryReq);
          }),
          catchError(refreshError => {
            // Si le refresh échoue, forcer la déconnexion
            authService.forceLogout();
            return throwError(() => refreshError);
          })
        );
      }

      // Si erreur 401 sans refresh token possible, forcer la déconnexion
      if (error.status === 401) {
        authService.forceLogout();
      }

      return throwError(() => error);
    })
  );
};

