# Dockerfile pour le frontend Angular PerformaAssist
FROM node:20-alpine as builder

# Métadonnées
LABEL maintainer="PerformaAssist Team"
LABEL description="Frontend Angular pour PerformaAssist"
LABEL version="1.0.0"

# Variables d'environnement pour la construction
ENV NODE_ENV=production
ENV NG_CLI_ANALYTICS=false

# Installer Angular CLI globalement
RUN npm install -g @angular/cli@17

# Répertoire de travail
WORKDIR /app

# Copier les fichiers de configuration
COPY package*.json ./

# Installer les dépendances
RUN npm ci --only=production

# Copier le code source
COPY . .

# Construire l'application pour la production
RUN ng build --configuration=production

# Image de production avec Nginx
FROM nginx:alpine

# Métadonnées pour l'image de production
LABEL maintainer="PerformaAssist Team"
LABEL description="Frontend Angular pour PerformaAssist - Production"
LABEL version="1.0.0"

# Supprimer la configuration par défaut de Nginx
RUN rm -rf /usr/share/nginx/html/*

# Copier les fichiers construits depuis l'étape de construction
COPY --from=builder /app/dist/performaassist-frontend /usr/share/nginx/html

# Copier la configuration Nginx personnalisée
COPY nginx.conf /etc/nginx/nginx.conf

# Créer un utilisateur non-root pour la sécurité
RUN addgroup -g 1001 -S nginx && \
    adduser -S -D -H -u 1001 -h /var/cache/nginx -s /sbin/nologin -G nginx -g nginx nginx

# Exposer le port
EXPOSE 80

# Vérification de santé
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
    CMD wget --no-verbose --tries=1 --spider http://localhost/ || exit 1

# Démarrer Nginx
CMD ["nginx", "-g", "daemon off;"]

