# Documentation API PerformaAssist

Cette documentation décrit l'API REST de PerformaAssist, permettant l'intégration avec des systèmes tiers et le développement d'applications personnalisées.

## Table des matières

1. [Vue d'ensemble](#vue-densemble)
2. [Authentification](#authentification)
3. [Endpoints d'authentification](#endpoints-dauthentification)
4. [Endpoints Checklists](#endpoints-checklists)
5. [Endpoints Plans d'action](#endpoints-plans-daction)
6. [Endpoints Réunions](#endpoints-réunions)
7. [Endpoints KPI](#endpoints-kpi)
8. [Endpoints Documents](#endpoints-documents)
9. [Gestion des erreurs](#gestion-des-erreurs)
10. [Exemples d'intégration](#exemples-dintégration)

## Vue d'ensemble

### URL de base

```
Production: https://api.performaassist.com
Développement: http://localhost:8080/api
```

### Format des données

- **Content-Type** : `application/json`
- **Charset** : UTF-8
- **Format des dates** : ISO 8601 (`2024-01-25T10:30:00Z`)
- **Format des UUID** : Standard UUID v4

### Versioning

L'API utilise un versioning par URL :
- Version actuelle : `/api/v1/`
- Rétrocompatibilité garantie pour les versions majeures

### Pagination

Les endpoints retournant des listes utilisent une pagination basée sur les paramètres :

```json
{
  "content": [...],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 150,
    "totalPages": 8
  }
}
```

Paramètres de pagination :
- `page` : Numéro de page (commence à 0)
- `size` : Nombre d'éléments par page (max 100)
- `sort` : Critère de tri (`field,direction`)

## Authentification

PerformaAssist utilise l'authentification JWT (JSON Web Token) avec un système de refresh token.

### Flux d'authentification

1. **Login** : Obtenir les tokens avec email/mot de passe
2. **Utilisation** : Inclure le token d'accès dans les requêtes
3. **Refresh** : Renouveler le token d'accès avec le refresh token
4. **Logout** : Invalider les tokens

### Headers requis

```http
Authorization: Bearer <access_token>
Content-Type: application/json
```

### Durée de vie des tokens

- **Access Token** : 15 minutes
- **Refresh Token** : 7 jours

## Endpoints d'authentification

### POST /api/auth/login

Authentifie un utilisateur et retourne les tokens JWT.

**Requête :**
```json
{
  "email": "manager@performaassist.com",
  "password": "password123"
}
```

**Réponse (200 OK) :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900,
  "user": {
    "id": "550e8400-e29b-41d4-a716-446655440011",
    "email": "manager@performaassist.com",
    "firstName": "Jean",
    "lastName": "Dupont",
    "role": "MANAGER",
    "siteId": "550e8400-e29b-41d4-a716-446655440001"
  }
}
```

**Erreurs :**
- `400 Bad Request` : Données invalides
- `401 Unauthorized` : Identifiants incorrects
- `423 Locked` : Compte désactivé

### POST /api/auth/refresh

Renouvelle le token d'accès avec le refresh token.

**Requête :**
```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

**Réponse (200 OK) :**
```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "tokenType": "Bearer",
  "expiresIn": 900
}
```

### POST /api/auth/logout

Invalide les tokens de l'utilisateur.

**Headers requis :**
```http
Authorization: Bearer <access_token>
```

**Réponse (200 OK) :**
```json
{
  "message": "Déconnexion réussie"
}
```

### GET /api/auth/profile

Retourne les informations du profil utilisateur connecté.

**Headers requis :**
```http
Authorization: Bearer <access_token>
```

**Réponse (200 OK) :**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440011",
  "email": "manager@performaassist.com",
  "firstName": "Jean",
  "lastName": "Dupont",
  "role": "MANAGER",
  "siteId": "550e8400-e29b-41d4-a716-446655440001",
  "isActive": true,
  "lastLoginAt": "2024-01-25T10:30:00Z",
  "createdAt": "2024-01-01T00:00:00Z"
}
```

## Endpoints Checklists

### GET /api/checklist-templates

Récupère la liste des modèles de checklist.

**Paramètres de requête :**
- `page` : Numéro de page (défaut: 0)
- `size` : Taille de page (défaut: 20)
- `search` : Recherche par nom ou description
- `category` : Filtrer par catégorie
- `createdBy` : Filtrer par créateur

**Exemple :**
```http
GET /api/checklist-templates?page=0&size=10&category=Sécurité
```

**Réponse (200 OK) :**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440020",
      "name": "Checklist Sécurité Quotidienne",
      "description": "Vérifications de sécurité à effectuer quotidiennement",
      "category": "Sécurité",
      "createdBy": "550e8400-e29b-41d4-a716-446655440011",
      "createdByName": "Jean Dupont",
      "isActive": true,
      "itemCount": 4,
      "createdAt": "2024-01-01T00:00:00Z",
      "updatedAt": "2024-01-01T00:00:00Z"
    }
  ],
  "page": {
    "number": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### GET /api/checklist-templates/{id}

Récupère un modèle de checklist par son ID.

**Réponse (200 OK) :**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440020",
  "name": "Checklist Sécurité Quotidienne",
  "description": "Vérifications de sécurité à effectuer quotidiennement",
  "category": "Sécurité",
  "createdBy": "550e8400-e29b-41d4-a716-446655440011",
  "createdByName": "Jean Dupont",
  "isActive": true,
  "items": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440030",
      "orderIndex": 1,
      "title": "Vérification des EPI",
      "description": "Tous les équipements de protection individuelle sont-ils disponibles et en bon état ?",
      "isMandatory": true,
      "scoringType": "BINARY",
      "maxScore": 1
    }
  ],
  "createdAt": "2024-01-01T00:00:00Z",
  "updatedAt": "2024-01-01T00:00:00Z"
}
```

### POST /api/checklist-templates

Crée un nouveau modèle de checklist.

**Autorisation requise :** MANAGER ou ADMIN

**Requête :**
```json
{
  "name": "Audit 5S Atelier",
  "description": "Checklist pour l'audit 5S des postes de travail",
  "category": "5S",
  "items": [
    {
      "orderIndex": 1,
      "title": "Seiri (Débarrasser)",
      "description": "Le poste ne contient que le nécessaire",
      "isMandatory": true,
      "scoringType": "SCALE",
      "maxScore": 5
    },
    {
      "orderIndex": 2,
      "title": "Seiton (Ranger)",
      "description": "Chaque chose a sa place et est à sa place",
      "isMandatory": true,
      "scoringType": "SCALE",
      "maxScore": 5
    }
  ]
}
```

**Réponse (201 Created) :**
```json
{
  "id": "550e8400-e29b-41d4-a716-446655440025",
  "name": "Audit 5S Atelier",
  "description": "Checklist pour l'audit 5S des postes de travail",
  "category": "5S",
  "createdBy": "550e8400-e29b-41d4-a716-446655440011",
  "createdByName": "Jean Dupont",
  "isActive": true,
  "items": [...],
  "createdAt": "2024-01-25T10:30:00Z",
  "updatedAt": "2024-01-25T10:30:00Z"
}
```

### PUT /api/checklist-templates/{id}

Met à jour un modèle de checklist existant.

**Autorisation requise :** MANAGER ou ADMIN (créateur ou admin)

**Requête :** Même format que POST

**Réponse (200 OK) :** Modèle mis à jour

### DELETE /api/checklist-templates/{id}

Supprime (désactive) un modèle de checklist.

**Autorisation requise :** MANAGER ou ADMIN (créateur ou admin)

**Réponse (204 No Content)**

### GET /api/checklist-templates/categories

Récupère la liste des catégories disponibles.

**Réponse (200 OK) :**
```json
[
  "Sécurité",
  "Qualité",
  "5S",
  "Environnement",
  "Maintenance"
]
```

### GET /api/checklist-templates/my-templates

Récupère les modèles créés par l'utilisateur connecté.

**Réponse (200 OK) :** Même format que GET /api/checklist-templates

### GET /api/checklist-templates/recent

Récupère les modèles récemment créés ou modifiés.

**Paramètres de requête :**
- `limit` : Nombre de résultats (défaut: 10, max: 50)

**Réponse (200 OK) :** Liste des modèles récents

## Endpoints Plans d'action

### GET /api/action-plans

Récupère la liste des plans d'action.

**Paramètres de requête :**
- `page`, `size` : Pagination
- `status` : Filtrer par statut (TODO, IN_PROGRESS, COMPLETED)
- `assigneeId` : Filtrer par assigné
- `priority` : Filtrer par priorité (HIGH, MEDIUM, LOW)
- `dueDateFrom`, `dueDateTo` : Filtrer par échéance

**Réponse (200 OK) :**
```json
{
  "content": [
    {
      "id": "550e8400-e29b-41d4-a716-446655440060",
      "title": "Réparer machine ligne 3",
      "description": "La machine présente des dysfonctionnements",
      "status": "IN_PROGRESS",
      "priority": "HIGH",
      "assigneeId": "550e8400-e29b-41d4-a716-446655440013",
      "assigneeName": "Pierre Durand",
      "createdBy": "550e8400-e29b-41d4-a716-446655440011",
      "createdByName": "Jean Dupont",
      "dueDate": "2024-01-30T00:00:00Z",
      "createdAt": "2024-01-25T10:30:00Z",
      "updatedAt": "2024-01-25T14:15:00Z"
    }
  ],
  "page": {
    "number": 0,
    "size": 20,
    "totalElements": 1,
    "totalPages": 1
  }
}
```

### POST /api/action-plans

Crée un nouveau plan d'action.

**Requête :**
```json
{
  "title": "Former équipe sur nouvelle procédure",
  "description": "Organiser une formation sur la nouvelle procédure de sécurité",
  "priority": "MEDIUM",
  "assigneeId": "550e8400-e29b-41d4-a716-446655440013",
  "dueDate": "2024-02-15T00:00:00Z"
}
```

**Réponse (201 Created) :** Plan d'action créé

### PUT /api/action-plans/{id}

Met à jour un plan d'action.

**PUT /api/action-plans/{id}/status**

Met à jour uniquement le statut d'un plan d'action.

**Requête :**
```json
{
  "status": "COMPLETED",
  "comment": "Tâche terminée avec succès"
}
```

## Endpoints Réunions

### GET /api/meetings

Récupère la liste des réunions.

### POST /api/meetings

Crée une nouvelle réunion.

**Requête :**
```json
{
  "title": "Réunion performance hebdomadaire",
  "description": "Point hebdomadaire sur les indicateurs",
  "scheduledDate": "2024-01-30T09:00:00Z",
  "duration": 60,
  "location": "Salle de réunion A",
  "agenda": [
    "Revue des KPI de la semaine",
    "Points d'amélioration",
    "Actions à mettre en place"
  ],
  "participantIds": [
    "550e8400-e29b-41d4-a716-446655440013",
    "550e8400-e29b-41d4-a716-446655440014"
  ]
}
```

## Endpoints KPI

### GET /api/kpis

Récupère la liste des indicateurs KPI.

### POST /api/kpis

Crée un nouvel indicateur KPI.

**Requête :**
```json
{
  "name": "Taux de conformité qualité",
  "description": "Pourcentage de pièces conformes",
  "unit": "%",
  "target": 98.5,
  "category": "Qualité",
  "frequency": "DAILY"
}
```

### POST /api/kpis/{id}/values

Ajoute une valeur à un indicateur KPI.

**Requête :**
```json
{
  "value": 97.2,
  "date": "2024-01-25T00:00:00Z",
  "comment": "Légère baisse due à problème machine"
}
```

## Endpoints Documents

### GET /api/documents

Récupère la liste des documents.

### POST /api/documents

Télécharge un nouveau document.

**Content-Type :** `multipart/form-data`

**Paramètres :**
- `file` : Fichier à télécharger
- `title` : Titre du document
- `description` : Description
- `category` : Catégorie
- `tags` : Tags (séparés par des virgules)

### GET /api/documents/{id}/download

Télécharge un document.

## Gestion des erreurs

### Codes de statut HTTP

| Code | Signification | Description |
|------|---------------|-------------|
| 200 | OK | Requête réussie |
| 201 | Created | Ressource créée |
| 204 | No Content | Suppression réussie |
| 400 | Bad Request | Données invalides |
| 401 | Unauthorized | Non authentifié |
| 403 | Forbidden | Accès interdit |
| 404 | Not Found | Ressource non trouvée |
| 409 | Conflict | Conflit (ex: email déjà utilisé) |
| 422 | Unprocessable Entity | Erreur de validation |
| 500 | Internal Server Error | Erreur serveur |

### Format des erreurs

```json
{
  "timestamp": "2024-01-25T10:30:00Z",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/checklist-templates",
  "details": [
    {
      "field": "name",
      "message": "Le nom est requis"
    },
    {
      "field": "items",
      "message": "Au moins un élément est requis"
    }
  ]
}
```

## Exemples d'intégration

### JavaScript/TypeScript

```typescript
class PerformaAssistAPI {
  private baseUrl = 'http://localhost:8080/api';
  private accessToken: string | null = null;

  async login(email: string, password: string) {
    const response = await fetch(`${this.baseUrl}/auth/login`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ email, password })
    });

    if (response.ok) {
      const data = await response.json();
      this.accessToken = data.accessToken;
      return data;
    }
    throw new Error('Login failed');
  }

  async getChecklistTemplates(params = {}) {
    const queryString = new URLSearchParams(params).toString();
    const response = await fetch(`${this.baseUrl}/checklist-templates?${queryString}`, {
      headers: {
        'Authorization': `Bearer ${this.accessToken}`,
        'Content-Type': 'application/json'
      }
    });

    if (response.ok) {
      return await response.json();
    }
    throw new Error('Failed to fetch templates');
  }

  async createActionPlan(actionPlan: any) {
    const response = await fetch(`${this.baseUrl}/action-plans`, {
      method: 'POST',
      headers: {
        'Authorization': `Bearer ${this.accessToken}`,
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(actionPlan)
    });

    if (response.ok) {
      return await response.json();
    }
    throw new Error('Failed to create action plan');
  }
}

// Utilisation
const api = new PerformaAssistAPI();
await api.login('manager@performaassist.com', 'password123');
const templates = await api.getChecklistTemplates({ category: 'Sécurité' });
```

### Python

```python
import requests
import json

class PerformaAssistAPI:
    def __init__(self, base_url='http://localhost:8080/api'):
        self.base_url = base_url
        self.access_token = None
        self.session = requests.Session()

    def login(self, email, password):
        response = self.session.post(
            f'{self.base_url}/auth/login',
            json={'email': email, 'password': password}
        )
        response.raise_for_status()
        
        data = response.json()
        self.access_token = data['accessToken']
        self.session.headers.update({
            'Authorization': f'Bearer {self.access_token}'
        })
        return data

    def get_checklist_templates(self, **params):
        response = self.session.get(
            f'{self.base_url}/checklist-templates',
            params=params
        )
        response.raise_for_status()
        return response.json()

    def create_action_plan(self, action_plan):
        response = self.session.post(
            f'{self.base_url}/action-plans',
            json=action_plan
        )
        response.raise_for_status()
        return response.json()

# Utilisation
api = PerformaAssistAPI()
api.login('manager@performaassist.com', 'password123')

templates = api.get_checklist_templates(category='Sécurité')
print(f"Trouvé {len(templates['content'])} modèles")

action_plan = {
    'title': 'Test API',
    'description': 'Plan créé via API',
    'priority': 'MEDIUM',
    'assigneeId': '550e8400-e29b-41d4-a716-446655440013',
    'dueDate': '2024-02-01T00:00:00Z'
}
result = api.create_action_plan(action_plan)
print(f"Plan créé avec ID: {result['id']}")
```

### cURL

```bash
# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"manager@performaassist.com","password":"password123"}' \
  | jq -r '.accessToken' > token.txt

# Utiliser le token
TOKEN=$(cat token.txt)

# Récupérer les modèles de checklist
curl -X GET "http://localhost:8080/api/checklist-templates?category=Sécurité" \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json"

# Créer un plan d'action
curl -X POST http://localhost:8080/api/action-plans \
  -H "Authorization: Bearer $TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test cURL",
    "description": "Plan créé via cURL",
    "priority": "LOW",
    "assigneeId": "550e8400-e29b-41d4-a716-446655440013",
    "dueDate": "2024-02-01T00:00:00Z"
  }'
```

## Limites et quotas

### Limites de taux

- **Authentification** : 5 tentatives par minute par IP
- **API générale** : 1000 requêtes par heure par utilisateur
- **Upload de fichiers** : 10 fichiers par minute par utilisateur

### Limites de taille

- **Requête JSON** : 1 MB maximum
- **Upload de fichier** : 50 MB par fichier
- **Nombre d'éléments** : 100 éléments maximum par checklist

### Headers de limite

Les réponses incluent des headers informatifs :

```http
X-RateLimit-Limit: 1000
X-RateLimit-Remaining: 999
X-RateLimit-Reset: 1640995200
```

## Webhooks (à venir)

PerformaAssist supportera les webhooks pour notifier les systèmes externes des événements importants :

- Création/modification de checklist
- Changement de statut de plan d'action
- Nouvelle réunion planifiée
- Seuil KPI dépassé

---

*Cette documentation API est maintenue par l'équipe PerformaAssist. Pour des questions ou suggestions, contactez api-support@performaassist.com*

