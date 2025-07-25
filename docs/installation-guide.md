# Guide d'installation PerformaAssist

Ce guide détaille les différentes méthodes d'installation de PerformaAssist, de l'installation rapide avec Docker à l'installation manuelle pour le développement.

## Table des matières

1. [Prérequis système](#prérequis-système)
2. [Installation rapide avec Docker](#installation-rapide-avec-docker)
3. [Installation pour le développement](#installation-pour-le-développement)
4. [Configuration avancée](#configuration-avancée)
5. [Vérification de l'installation](#vérification-de-linstallation)
6. [Dépannage](#dépannage)

## Prérequis système

### Configuration minimale

| Composant | Minimum | Recommandé |
|-----------|---------|------------|
| **CPU** | 2 cores | 4 cores |
| **RAM** | 4 GB | 8 GB |
| **Stockage** | 10 GB | 20 GB |
| **OS** | Ubuntu 20.04, CentOS 8, Windows 10 | Ubuntu 22.04, Windows 11 |

### Logiciels requis

#### Pour l'installation Docker (recommandée)
- **Docker** 20.10+ ([Installation Docker](https://docs.docker.com/get-docker/))
- **Docker Compose** 2.0+ ([Installation Compose](https://docs.docker.com/compose/install/))
- **Git** pour cloner le repository

#### Pour l'installation manuelle
- **Java 21** ([OpenJDK](https://openjdk.org/projects/jdk/21/) ou [Oracle JDK](https://www.oracle.com/java/technologies/downloads/))
- **Node.js 20+** et **npm** ([Installation Node.js](https://nodejs.org/))
- **PostgreSQL 15+** ([Installation PostgreSQL](https://www.postgresql.org/download/))
- **Maven 3.9+** ([Installation Maven](https://maven.apache.org/install.html))

### Vérification des prérequis

```bash
# Vérifier Docker
docker --version
docker-compose --version

# Vérifier Java (pour installation manuelle)
java --version
mvn --version

# Vérifier Node.js (pour installation manuelle)
node --version
npm --version

# Vérifier PostgreSQL (pour installation manuelle)
psql --version
```

## Installation rapide avec Docker

### Étape 1 : Cloner le repository

```bash
# Cloner le projet
git clone https://github.com/performaassist/performaassist.git
cd performaassist

# Vérifier le contenu
ls -la
```

### Étape 2 : Configuration (optionnelle)

Créer un fichier `.env` pour personnaliser la configuration :

```bash
# Copier le fichier d'exemple
cp .env.example .env

# Éditer la configuration
nano .env
```

Exemple de fichier `.env` :

```env
# Configuration de base
COMPOSE_PROJECT_NAME=performaassist
ENVIRONMENT=production

# Base de données
POSTGRES_DB=performaassist
POSTGRES_USER=performaassist
POSTGRES_PASSWORD=your_secure_password_here

# JWT Configuration
JWT_SECRET=your_super_secret_jwt_key_here_minimum_32_characters
JWT_EXPIRATION_MS=900000
JWT_REFRESH_EXPIRATION_MS=604800000

# Ports (modifier si nécessaire)
FRONTEND_PORT=80
BACKEND_PORT=8080
DATABASE_PORT=5432
ADMINER_PORT=8081
```

### Étape 3 : Lancement de l'application

```bash
# Méthode 1 : Utiliser le script de déploiement (recommandé)
./scripts/deploy.sh

# Méthode 2 : Docker Compose direct
docker-compose up -d

# Méthode 3 : Avec profil de développement
./scripts/deploy.sh development
```

### Étape 4 : Vérification du déploiement

```bash
# Vérifier l'état des conteneurs
docker-compose ps

# Vérifier les logs
docker-compose logs -f

# Tester la connectivité
curl http://localhost:8080/actuator/health
curl http://localhost/health
```

## Installation pour le développement

### Étape 1 : Préparer l'environnement

```bash
# Cloner et entrer dans le projet
git clone https://github.com/performaassist/performaassist.git
cd performaassist

# Installer les dépendances système (Ubuntu/Debian)
sudo apt update
sudo apt install -y openjdk-21-jdk nodejs npm postgresql-15 maven git
```

### Étape 2 : Configuration de la base de données

```bash
# Démarrer PostgreSQL
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Créer la base de données
sudo -u postgres psql << EOF
CREATE DATABASE performaassist_dev;
CREATE USER dev_user WITH PASSWORD 'dev_password';
GRANT ALL PRIVILEGES ON DATABASE performaassist_dev TO dev_user;
ALTER USER dev_user CREATEDB;
\q
EOF
```

### Étape 3 : Configuration du backend

```bash
cd backend

# Copier la configuration de développement
cp src/main/resources/application-dev.yml.example src/main/resources/application-dev.yml

# Éditer la configuration
nano src/main/resources/application-dev.yml
```

Configuration `application-dev.yml` :

```yaml
spring:
  profiles:
    active: dev
  
  datasource:
    url: jdbc:postgresql://localhost:5432/performaassist_dev
    username: dev_user
    password: dev_password
    driver-class-name: org.postgresql.Driver
  
  jpa:
    hibernate:
      ddl-auto: update
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  liquibase:
    change-log: classpath:db/changelog/db.changelog-master.xml
    enabled: true

jwt:
  secret: dev-secret-key-for-development-only-not-for-production
  expiration-ms: 3600000  # 1 heure en dev
  refresh-expiration-ms: 604800000  # 7 jours

logging:
  level:
    com.performaassist: DEBUG
    org.springframework.security: DEBUG
```

### Étape 4 : Démarrage du backend

```bash
# Compiler et démarrer
mvn clean compile
mvn spring-boot:run

# Ou en mode debug
mvn spring-boot:run -Dspring-boot.run.jvmArguments="-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=n,address=5005"
```

### Étape 5 : Configuration du frontend

```bash
cd ../frontend/performaassist-frontend

# Installer les dépendances
npm install

# Copier la configuration de développement
cp src/environments/environment.dev.ts.example src/environments/environment.dev.ts

# Éditer la configuration
nano src/environments/environment.dev.ts
```

Configuration `environment.dev.ts` :

```typescript
export const environment = {
  production: false,
  apiUrl: 'http://localhost:8080/api',
  enableDebug: true,
  defaultLanguage: 'fr',
  supportedLanguages: ['fr', 'ar']
};
```

### Étape 6 : Démarrage du frontend

```bash
# Démarrer en mode développement
npm start

# Ou avec configuration spécifique
ng serve --configuration=development --host=0.0.0.0 --port=4200
```

### Étape 7 : Initialisation des données de test

```bash
# Exécuter le script de données de test
psql -h localhost -U dev_user -d performaassist_dev -f scripts/database/dev-data.sql
```

## Configuration avancée

### Configuration SSL/HTTPS

Pour activer HTTPS en production :

1. **Obtenir des certificats SSL**

```bash
# Avec Let's Encrypt (certbot)
sudo apt install certbot
sudo certbot certonly --standalone -d your-domain.com
```

2. **Configurer Nginx**

Créer `nginx-ssl.conf` :

```nginx
server {
    listen 443 ssl http2;
    server_name your-domain.com;
    
    ssl_certificate /etc/letsencrypt/live/your-domain.com/fullchain.pem;
    ssl_certificate_key /etc/letsencrypt/live/your-domain.com/privkey.pem;
    
    # Configuration SSL moderne
    ssl_protocols TLSv1.2 TLSv1.3;
    ssl_ciphers ECDHE-RSA-AES256-GCM-SHA512:DHE-RSA-AES256-GCM-SHA512;
    ssl_prefer_server_ciphers off;
    
    # Headers de sécurité
    add_header Strict-Transport-Security "max-age=63072000" always;
    add_header X-Frame-Options DENY;
    add_header X-Content-Type-Options nosniff;
    
    # Configuration de l'application
    location / {
        try_files $uri $uri/ /index.html;
    }
    
    location /api/ {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
        proxy_set_header X-Forwarded-Proto $scheme;
    }
}

# Redirection HTTP vers HTTPS
server {
    listen 80;
    server_name your-domain.com;
    return 301 https://$server_name$request_uri;
}
```

### Configuration de monitoring

1. **Prometheus et Grafana**

Ajouter au `docker-compose.yml` :

```yaml
  prometheus:
    image: prom/prometheus:latest
    container_name: performaassist-prometheus
    ports:
      - "9090:9090"
    volumes:
      - ./monitoring/prometheus.yml:/etc/prometheus/prometheus.yml
    networks:
      - performaassist-network

  grafana:
    image: grafana/grafana:latest
    container_name: performaassist-grafana
    ports:
      - "3000:3000"
    environment:
      - GF_SECURITY_ADMIN_PASSWORD=admin
    volumes:
      - grafana_data:/var/lib/grafana
    networks:
      - performaassist-network
```

2. **Configuration Prometheus** (`monitoring/prometheus.yml`) :

```yaml
global:
  scrape_interval: 15s

scrape_configs:
  - job_name: 'performaassist-backend'
    static_configs:
      - targets: ['backend:8080']
    metrics_path: '/actuator/prometheus'
```

### Configuration de sauvegarde automatique

Créer un script de sauvegarde automatique :

```bash
#!/bin/bash
# scripts/backup-cron.sh

BACKUP_DIR="/var/backups/performaassist"
DATE=$(date +%Y%m%d_%H%M%S)
BACKUP_FILE="$BACKUP_DIR/performaassist_$DATE.sql"

# Créer le répertoire de sauvegarde
mkdir -p $BACKUP_DIR

# Effectuer la sauvegarde
docker-compose exec -T database pg_dump -U performaassist performaassist > $BACKUP_FILE

# Compresser la sauvegarde
gzip $BACKUP_FILE

# Supprimer les sauvegardes de plus de 30 jours
find $BACKUP_DIR -name "*.sql.gz" -mtime +30 -delete

echo "Sauvegarde terminée : $BACKUP_FILE.gz"
```

Ajouter au crontab :

```bash
# Éditer le crontab
crontab -e

# Ajouter la ligne pour sauvegarde quotidienne à 2h du matin
0 2 * * * /path/to/performaassist/scripts/backup-cron.sh
```

## Vérification de l'installation

### Tests de connectivité

```bash
# Test du backend
curl -X GET http://localhost:8080/actuator/health
# Réponse attendue : {"status":"UP"}

# Test du frontend
curl -X GET http://localhost/health
# Réponse attendue : healthy

# Test de l'API d'authentification
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@performaassist.com","password":"password123"}'
```

### Tests fonctionnels

1. **Accéder à l'interface** : http://localhost
2. **Se connecter** avec les comptes de test
3. **Naviguer** dans les différents modules
4. **Créer** une checklist de test
5. **Vérifier** les logs pour les erreurs

### Vérification des performances

```bash
# Utiliser Apache Bench pour tester les performances
ab -n 100 -c 10 http://localhost:8080/api/auth/login

# Utiliser curl pour mesurer les temps de réponse
curl -w "@curl-format.txt" -o /dev/null -s http://localhost/
```

Fichier `curl-format.txt` :

```
     time_namelookup:  %{time_namelookup}\n
        time_connect:  %{time_connect}\n
     time_appconnect:  %{time_appconnect}\n
    time_pretransfer:  %{time_pretransfer}\n
       time_redirect:  %{time_redirect}\n
  time_starttransfer:  %{time_starttransfer}\n
                     ----------\n
          time_total:  %{time_total}\n
```

## Dépannage

### Problèmes courants

#### 1. Erreur de connexion à la base de données

**Symptôme** : `Connection refused` ou `Authentication failed`

**Solutions** :
```bash
# Vérifier que PostgreSQL fonctionne
docker-compose ps database

# Vérifier les logs de la base
docker-compose logs database

# Redémarrer la base de données
docker-compose restart database

# Vérifier la configuration
docker-compose exec database psql -U performaassist -d performaassist -c "\l"
```

#### 2. Erreur de compilation du frontend

**Symptôme** : Erreurs TypeScript ou Angular

**Solutions** :
```bash
# Nettoyer le cache npm
cd frontend/performaassist-frontend
npm cache clean --force
rm -rf node_modules package-lock.json
npm install

# Vérifier la version de Node.js
node --version  # Doit être 20+

# Compiler en mode verbose
npm run build -- --verbose
```

#### 3. Problème de permissions Docker

**Symptôme** : `Permission denied` lors de l'exécution Docker

**Solutions** :
```bash
# Ajouter l'utilisateur au groupe docker
sudo usermod -aG docker $USER
newgrp docker

# Ou utiliser sudo temporairement
sudo docker-compose up -d
```

#### 4. Port déjà utilisé

**Symptôme** : `Port already in use`

**Solutions** :
```bash
# Identifier le processus utilisant le port
sudo lsof -i :8080
sudo lsof -i :80

# Arrêter le processus ou modifier la configuration
# Dans docker-compose.yml, changer les ports :
ports:
  - "8081:8080"  # Au lieu de 8080:8080
```

#### 5. Problème de mémoire

**Symptôme** : `OutOfMemoryError` ou conteneurs qui s'arrêtent

**Solutions** :
```bash
# Augmenter la mémoire Docker (Docker Desktop)
# Paramètres > Resources > Memory : 8GB minimum

# Ou modifier les limites dans docker-compose.yml
services:
  backend:
    deploy:
      resources:
        limits:
          memory: 2G
        reservations:
          memory: 1G
```

### Logs et diagnostic

```bash
# Voir tous les logs
docker-compose logs -f

# Logs d'un service spécifique
docker-compose logs -f backend
docker-compose logs -f frontend
docker-compose logs -f database

# Logs avec timestamps
docker-compose logs -f -t

# Entrer dans un conteneur pour diagnostic
docker-compose exec backend bash
docker-compose exec database psql -U performaassist -d performaassist
```

### Réinitialisation complète

En cas de problème majeur, réinitialiser complètement :

```bash
# Arrêter et supprimer tous les conteneurs
docker-compose down -v

# Supprimer les images
docker-compose down --rmi all

# Nettoyer Docker
docker system prune -a

# Redémarrer l'installation
./scripts/deploy.sh
```

## Support

Si vous rencontrez des problèmes non couverts par ce guide :

1. **Consulter** la [documentation complète](../README.md)
2. **Rechercher** dans les [issues GitHub](https://github.com/performaassist/performaassist/issues)
3. **Créer** une nouvelle issue avec :
   - Description détaillée du problème
   - Logs d'erreur complets
   - Configuration système
   - Étapes pour reproduire

4. **Contacter** le support : support@performaassist.com

---

*Ce guide d'installation est maintenu par l'équipe PerformaAssist. Dernière mise à jour : Janvier 2024*

