#!/bin/bash

# Script de déploiement pour PerformaAssist
# Usage: ./scripts/deploy.sh [environment]

set -e

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
PROJECT_DIR="$(dirname "$SCRIPT_DIR")"
ENVIRONMENT="${1:-production}"

# Couleurs pour les messages
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonctions utilitaires
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Vérification des prérequis
check_prerequisites() {
    log_info "Vérification des prérequis..."
    
    if ! command -v docker &> /dev/null; then
        log_error "Docker n'est pas installé"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        log_error "Docker Compose n'est pas installé"
        exit 1
    fi
    
    log_success "Prérequis vérifiés"
}

# Construction des images
build_images() {
    log_info "Construction des images Docker..."
    
    cd "$PROJECT_DIR"
    
    # Construction du backend
    log_info "Construction de l'image backend..."
    docker build -t performaassist-backend:latest ./backend
    
    # Construction du frontend
    log_info "Construction de l'image frontend..."
    docker build -t performaassist-frontend:latest ./frontend/performaassist-frontend
    
    log_success "Images construites avec succès"
}

# Déploiement
deploy() {
    log_info "Déploiement de l'environnement: $ENVIRONMENT"
    
    cd "$PROJECT_DIR"
    
    case $ENVIRONMENT in
        "development"|"dev")
            log_info "Déploiement en mode développement..."
            docker-compose -f docker-compose.yml -f docker-compose.dev.yml up -d
            ;;
        "production"|"prod")
            log_info "Déploiement en mode production..."
            docker-compose up -d
            ;;
        "staging")
            log_info "Déploiement en mode staging..."
            docker-compose -f docker-compose.yml up -d
            ;;
        *)
            log_error "Environnement non reconnu: $ENVIRONMENT"
            log_info "Environnements disponibles: development, staging, production"
            exit 1
            ;;
    esac
    
    log_success "Déploiement terminé"
}

# Vérification de l'état des services
check_health() {
    log_info "Vérification de l'état des services..."
    
    # Attendre que les services démarrent
    sleep 30
    
    # Vérifier la base de données
    if docker-compose exec -T database pg_isready -U performaassist -d performaassist; then
        log_success "Base de données: OK"
    else
        log_error "Base de données: ERREUR"
    fi
    
    # Vérifier le backend
    if curl -f http://localhost:8080/actuator/health &> /dev/null; then
        log_success "Backend: OK"
    else
        log_error "Backend: ERREUR"
    fi
    
    # Vérifier le frontend
    if curl -f http://localhost/health &> /dev/null; then
        log_success "Frontend: OK"
    else
        log_error "Frontend: ERREUR"
    fi
}

# Sauvegarde de la base de données
backup_database() {
    log_info "Sauvegarde de la base de données..."
    
    BACKUP_DIR="$PROJECT_DIR/backups"
    mkdir -p "$BACKUP_DIR"
    
    BACKUP_FILE="$BACKUP_DIR/performaassist_$(date +%Y%m%d_%H%M%S).sql"
    
    docker-compose exec -T database pg_dump -U performaassist performaassist > "$BACKUP_FILE"
    
    if [ $? -eq 0 ]; then
        log_success "Sauvegarde créée: $BACKUP_FILE"
    else
        log_error "Erreur lors de la sauvegarde"
        exit 1
    fi
}

# Restauration de la base de données
restore_database() {
    if [ -z "$2" ]; then
        log_error "Usage: $0 restore <backup_file>"
        exit 1
    fi
    
    BACKUP_FILE="$2"
    
    if [ ! -f "$BACKUP_FILE" ]; then
        log_error "Fichier de sauvegarde non trouvé: $BACKUP_FILE"
        exit 1
    fi
    
    log_info "Restauration de la base de données depuis: $BACKUP_FILE"
    
    docker-compose exec -T database psql -U performaassist -d performaassist < "$BACKUP_FILE"
    
    if [ $? -eq 0 ]; then
        log_success "Restauration terminée"
    else
        log_error "Erreur lors de la restauration"
        exit 1
    fi
}

# Nettoyage
cleanup() {
    log_info "Nettoyage des ressources..."
    
    cd "$PROJECT_DIR"
    
    # Arrêter les services
    docker-compose down
    
    # Supprimer les images non utilisées
    docker image prune -f
    
    # Supprimer les volumes orphelins
    docker volume prune -f
    
    log_success "Nettoyage terminé"
}

# Affichage des logs
show_logs() {
    SERVICE="${2:-}"
    
    cd "$PROJECT_DIR"
    
    if [ -n "$SERVICE" ]; then
        log_info "Affichage des logs pour le service: $SERVICE"
        docker-compose logs -f "$SERVICE"
    else
        log_info "Affichage de tous les logs"
        docker-compose logs -f
    fi
}

# Menu principal
main() {
    case "${1:-deploy}" in
        "deploy")
            check_prerequisites
            build_images
            deploy
            check_health
            ;;
        "build")
            check_prerequisites
            build_images
            ;;
        "start")
            deploy
            ;;
        "stop")
            cd "$PROJECT_DIR"
            docker-compose down
            log_success "Services arrêtés"
            ;;
        "restart")
            cd "$PROJECT_DIR"
            docker-compose restart
            log_success "Services redémarrés"
            ;;
        "status")
            cd "$PROJECT_DIR"
            docker-compose ps
            ;;
        "health")
            check_health
            ;;
        "backup")
            backup_database
            ;;
        "restore")
            restore_database "$@"
            ;;
        "cleanup")
            cleanup
            ;;
        "logs")
            show_logs "$@"
            ;;
        "help"|"-h"|"--help")
            echo "Usage: $0 [command] [options]"
            echo ""
            echo "Commandes disponibles:"
            echo "  deploy [env]     Déploie l'application (env: dev, staging, prod)"
            echo "  build            Construit les images Docker"
            echo "  start            Démarre les services"
            echo "  stop             Arrête les services"
            echo "  restart          Redémarre les services"
            echo "  status           Affiche l'état des services"
            echo "  health           Vérifie la santé des services"
            echo "  backup           Sauvegarde la base de données"
            echo "  restore <file>   Restaure la base de données"
            echo "  cleanup          Nettoie les ressources Docker"
            echo "  logs [service]   Affiche les logs"
            echo "  help             Affiche cette aide"
            ;;
        *)
            log_error "Commande non reconnue: $1"
            log_info "Utilisez '$0 help' pour voir les commandes disponibles"
            exit 1
            ;;
    esac
}

# Exécution du script
main "$@"

