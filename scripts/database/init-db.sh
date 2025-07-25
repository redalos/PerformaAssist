#!/bin/bash

# Script d'initialisation de la base de données PostgreSQL pour PerformaAssist
# Ce script crée la base de données, l'utilisateur et configure les permissions

set -e

# Variables de configuration
DB_NAME="performaassist"
DB_USER="performaassist"
DB_PASSWORD="performaassist123"
DB_HOST="localhost"
DB_PORT="5432"

echo "🚀 Initialisation de la base de données PerformaAssist..."

# Vérifier si PostgreSQL est installé
if ! command -v psql &> /dev/null; then
    echo "❌ PostgreSQL n'est pas installé. Installation en cours..."
    sudo apt update
    sudo apt install -y postgresql postgresql-contrib
    sudo systemctl start postgresql
    sudo systemctl enable postgresql
fi

# Créer l'utilisateur et la base de données
echo "📊 Création de l'utilisateur et de la base de données..."

sudo -u postgres psql << EOF
-- Créer l'utilisateur s'il n'existe pas
DO \$\$
BEGIN
    IF NOT EXISTS (SELECT FROM pg_catalog.pg_roles WHERE rolname = '$DB_USER') THEN
        CREATE USER $DB_USER WITH PASSWORD '$DB_PASSWORD';
    END IF;
END
\$\$;

-- Créer la base de données s'il n'existe pas
SELECT 'CREATE DATABASE $DB_NAME OWNER $DB_USER'
WHERE NOT EXISTS (SELECT FROM pg_database WHERE datname = '$DB_NAME')\gexec

-- Accorder tous les privilèges
GRANT ALL PRIVILEGES ON DATABASE $DB_NAME TO $DB_USER;
ALTER USER $DB_USER CREATEDB;

\q
EOF

echo "✅ Base de données '$DB_NAME' créée avec succès!"
echo "👤 Utilisateur: $DB_USER"
echo "🔗 URL de connexion: jdbc:postgresql://$DB_HOST:$DB_PORT/$DB_NAME"

# Tester la connexion
echo "🔍 Test de connexion..."
if PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -p $DB_PORT -U $DB_USER -d $DB_NAME -c "SELECT version();" > /dev/null 2>&1; then
    echo "✅ Connexion à la base de données réussie!"
else
    echo "❌ Échec de la connexion à la base de données"
    exit 1
fi

echo "🎉 Initialisation terminée avec succès!"

