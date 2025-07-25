# PerformaAssist

**Application SaaS de gestion managériale terrain pour l'excellence opérationnelle**

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/performaassist/performaassist)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](https://github.com/performaassist/performaassist/actions)
[![Docker](https://img.shields.io/badge/docker-ready-blue.svg)](https://hub.docker.com/r/performaassist/performaassist)

## 📋 Table des matières

- [Vue d'ensemble](#vue-densemble)
- [Fonctionnalités](#fonctionnalités)
- [Architecture technique](#architecture-technique)
- [Installation rapide](#installation-rapide)
- [Guide de déploiement](#guide-de-déploiement)
- [Documentation](#documentation)
- [Contribution](#contribution)
- [Support](#support)

## 🎯 Vue d'ensemble

PerformaAssist est une application SaaS moderne conçue pour révolutionner la gestion managériale terrain dans les entreprises. Inspirée des meilleures pratiques du Lean Management et du système UTrakk, elle permet aux managers et responsables d'équipe de piloter efficacement la performance opérationnelle, structurer les rituels managériaux et maintenir l'excellence opérationnelle au quotidien.

### Problématiques adressées

- **Manque de visibilité** sur les activités terrain et la performance opérationnelle
- **Absence de standardisation** des processus de contrôle et d'audit
- **Difficultés de suivi** des plans d'action et des engagements pris
- **Manque de traçabilité** des réunions managériales et des décisions
- **Dispersion des indicateurs** de performance et des données terrain
- **Absence de centralisation** de la documentation et des procédures

### Valeur ajoutée

PerformaAssist transforme la gestion managériale en offrant une plateforme unifiée qui digitalise et optimise tous les aspects du management terrain, de l'audit 5S aux réunions de performance, en passant par le suivi des plans d'action et la gestion documentaire.

## ✨ Fonctionnalités

### 🔐 Authentification et gestion des utilisateurs
- **Authentification sécurisée** par email et mot de passe avec JWT
- **Gestion des rôles** : Administrateur, Manager, Employé terrain
- **Gestion multi-sites** avec attribution des utilisateurs par site
- **Audit des connexions** et traçabilité des actions

### ✅ Module Checklists terrain (Gemba Walk)
- **Création de modèles** de checklists personnalisées par catégorie
- **Exécution mobile** optimisée pour les audits terrain
- **Types de notation** : Binaire (Oui/Non), Échelle (1-5), Pourcentage
- **Capture multimédia** : Photos, commentaires et observations
- **Historique complet** des exécutions et tendances

### 📋 Module Plan d'action
- **Création et assignation** de tâches avec responsables
- **Gestion des échéances** et notifications automatiques
- **Suivi du statut** : À faire, En cours, Terminé
- **Priorisation** : Haute, Moyenne, Basse
- **Historique des actions** et traçabilité complète

### 🤝 Module Réunions managériales
- **Planification** avec ordre du jour structuré
- **Prise de notes collaborative** en temps réel
- **Assignation automatique** d'actions de suivi
- **Gestion des participants** et convocations
- **Archivage** et recherche dans l'historique

### 📊 Module KPI / Indicateurs
- **Saisie manuelle** ou intégration future d'indicateurs
- **Visualisation graphique** : Lignes, barres, camemberts
- **Tableaux de bord** personnalisables par rôle
- **Filtrage** par site, utilisateur, période
- **Alertes** sur dépassement de seuils

### 📚 Centre de documentation
- **Gestion des SOP** (Standard Operating Procedures)
- **Téléchargement** de fichiers PDF, DOC, images
- **Recherche avancée** par mot-clé, catégorie, type
- **Versioning** et historique des modifications
- **Contrôle d'accès** par rôle et site

### 🌍 Internationalisation
- **Interface multilingue** : Français et Arabe
- **Support RTL** (Right-to-Left) pour l'arabe
- **Adaptation culturelle** des formats de date et nombres
- **Extensibilité** pour d'autres langues

## 🏗️ Architecture technique

### Stack technologique

**Backend**
- **Java 21** avec Spring Boot 3.2
- **Spring Security** avec authentification JWT
- **PostgreSQL 15** pour la persistance
- **Liquibase** pour la gestion des migrations
- **Architecture hexagonale** (Ports & Adapters)

**Frontend**
- **Angular 17** avec TypeScript
- **Tailwind CSS** pour le design system
- **Angular Material** pour les composants UI
- **PWA-ready** pour l'utilisation mobile
- **Lazy loading** pour les performances

**Infrastructure**
- **Docker** et Docker Compose pour la containerisation
- **Nginx** comme reverse proxy et serveur statique
- **Redis** pour la mise en cache (optionnel)
- **GitHub Actions** pour CI/CD

### Principes architecturaux

L'application respecte les principes de l'architecture hexagonale, garantissant une séparation claire entre la logique métier et les détails techniques. Cette approche facilite la maintenance, les tests et l'évolution de l'application.

```
┌─────────────────────────────────────────────────────────────┐
│                    Frontend Angular                         │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────────┐ │
│  │   Auth      │ │ Checklists  │ │    Other Modules        │ │
│  │   Module    │ │   Module    │ │  (Actions, KPI, etc.)   │ │
│  └─────────────┘ └─────────────┘ └─────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                         HTTP/REST API
                              │
┌─────────────────────────────────────────────────────────────┐
│                    Backend Spring Boot                      │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                Web Layer (Controllers)                  │ │
│  └─────────────────────────────────────────────────────────┘ │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │              Application Layer (Services)               │ │
│  └─────────────────────────────────────────────────────────┘ │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │                Domain Layer (Entities)                  │ │
│  └─────────────────────────────────────────────────────────┘ │
│  ┌─────────────────────────────────────────────────────────┐ │
│  │            Infrastructure Layer (Adapters)              │ │
│  └─────────────────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────────┘
                              │
                         JPA/Hibernate
                              │
┌─────────────────────────────────────────────────────────────┐
│                    PostgreSQL Database                      │
└─────────────────────────────────────────────────────────────┘
```

## 🚀 Installation rapide

### Prérequis

- **Docker** 20.10+ et **Docker Compose** 2.0+
- **Git** pour cloner le repository
- **8 GB RAM** minimum recommandé

### Installation en 3 étapes

1. **Cloner le repository**
```bash
git clone https://github.com/performaassist/performaassist.git
cd performaassist
```

2. **Lancer l'application**
```bash
./scripts/deploy.sh
```

3. **Accéder à l'application**
- Frontend : http://localhost
- Backend API : http://localhost:8080
- Base de données (Adminer) : http://localhost:8081

### Comptes de test

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| admin@performaassist.com | password123 | Administrateur |
| manager.paris@performaassist.com | password123 | Manager |
| employee1@performaassist.com | password123 | Employé |

## 📦 Guide de déploiement

### Environnement de développement

```bash
# Démarrage avec hot reload
./scripts/deploy.sh development

# Ou avec Docker Compose
docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
```

### Environnement de production

```bash
# Déploiement production
./scripts/deploy.sh production

# Avec variables d'environnement personnalisées
export JWT_SECRET="your-super-secret-key"
export POSTGRES_PASSWORD="your-secure-password"
./scripts/deploy.sh production
```

### Variables d'environnement

| Variable | Description | Valeur par défaut |
|----------|-------------|-------------------|
| `SPRING_PROFILES_ACTIVE` | Profil Spring actif | `docker` |
| `JWT_SECRET` | Clé secrète JWT | `performaassist-jwt-secret-key-2024` |
| `JWT_EXPIRATION_MS` | Durée de vie token (ms) | `900000` (15 min) |
| `POSTGRES_DB` | Nom de la base | `performaassist` |
| `POSTGRES_USER` | Utilisateur DB | `performaassist` |
| `POSTGRES_PASSWORD` | Mot de passe DB | `performaassist123` |

### Commandes utiles

```bash
# Voir les logs
./scripts/deploy.sh logs

# Sauvegarder la base
./scripts/deploy.sh backup

# Restaurer une sauvegarde
./scripts/deploy.sh restore backup_file.sql

# Arrêter l'application
./scripts/deploy.sh stop

# Nettoyer les ressources
./scripts/deploy.sh cleanup
```

## 📖 Documentation

### Guides utilisateur
- [Guide d'utilisation Manager](docs/user-guide-manager.md)
- [Guide d'utilisation Employé](docs/user-guide-employee.md)
- [Guide d'administration](docs/admin-guide.md)

### Documentation technique
- [Architecture détaillée](docs/architecture.md)
- [API Documentation](docs/api-documentation.md)
- [Guide de développement](docs/development-guide.md)
- [Guide de déploiement](docs/deployment-guide.md)

### Tutoriels
- [Créer sa première checklist](docs/tutorials/first-checklist.md)
- [Configurer les KPI](docs/tutorials/setup-kpi.md)
- [Organiser une réunion](docs/tutorials/meeting-setup.md)

## 🛠️ Développement

### Structure du projet

```
performaassist/
├── backend/                 # Application Spring Boot
│   ├── src/main/java/      # Code source Java
│   ├── src/main/resources/ # Ressources et configuration
│   └── Dockerfile          # Image Docker backend
├── frontend/               # Application Angular
│   ├── src/app/           # Code source TypeScript
│   ├── src/assets/        # Assets statiques
│   └── Dockerfile         # Image Docker frontend
├── scripts/               # Scripts d'automatisation
│   ├── deploy.sh         # Script de déploiement
│   └── database/         # Scripts de base de données
├── docs/                 # Documentation
├── docker-compose.yml    # Orchestration Docker
└── README.md            # Ce fichier
```

### Commandes de développement

```bash
# Backend (Spring Boot)
cd backend
mvn spring-boot:run

# Frontend (Angular)
cd frontend/performaassist-frontend
npm start

# Tests
mvn test                    # Tests backend
npm test                   # Tests frontend

# Build
mvn clean package          # Build backend
npm run build             # Build frontend
```

## 🤝 Contribution

Nous accueillons les contributions de la communauté ! Voici comment participer :

1. **Fork** le repository
2. **Créer** une branche feature (`git checkout -b feature/amazing-feature`)
3. **Commit** vos changements (`git commit -m 'Add amazing feature'`)
4. **Push** vers la branche (`git push origin feature/amazing-feature`)
5. **Ouvrir** une Pull Request

### Standards de code

- **Java** : Respect des conventions Oracle et Spring
- **TypeScript** : Utilisation d'ESLint et Prettier
- **Tests** : Couverture minimale de 80%
- **Documentation** : Commentaires JSDoc/JavaDoc obligatoires

## 📞 Support

### Communauté
- **GitHub Issues** : [Signaler un bug](https://github.com/performaassist/performaassist/issues)
- **Discussions** : [Forum communautaire](https://github.com/performaassist/performaassist/discussions)
- **Wiki** : [Base de connaissances](https://github.com/performaassist/performaassist/wiki)

### Support commercial
- **Email** : support@performaassist.com
- **Documentation** : https://docs.performaassist.com
- **Formation** : Nous proposons des formations personnalisées

## 📄 Licence

Ce projet est sous licence MIT. Voir le fichier [LICENSE](LICENSE) pour plus de détails.

## 🙏 Remerciements

- **Équipe de développement** PerformaAssist
- **Communauté Open Source** pour les outils et bibliothèques utilisés
- **Utilisateurs beta** pour leurs retours précieux
- **Contributeurs** qui améliorent continuellement le projet

---

**PerformaAssist** - *Transformez votre gestion managériale terrain*

Développé avec ❤️ par l'équipe PerformaAssist

