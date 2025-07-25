-- Script de données de développement pour PerformaAssist
-- Ce script insère des données de test pour faciliter le développement

-- Insertion des sites de test
INSERT INTO sites (id, name, description, address, city, country, postal_code, is_active, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440001', 'Site Principal Paris', 'Site principal de l''entreprise à Paris', '123 Avenue des Champs-Élysées', 'Paris', 'France', '75008', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440002', 'Site Lyon', 'Site secondaire à Lyon', '45 Rue de la République', 'Lyon', 'France', '69002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440003', 'Site Marseille', 'Site régional Marseille', '78 La Canebière', 'Marseille', 'France', '13001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertion des utilisateurs de test
-- Mot de passe pour tous: "password123" (encodé en BCrypt)
INSERT INTO users (id, email, password, first_name, last_name, role, site_id, is_active, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440010', 'admin@performaassist.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSZwZlKrE42jb6q5YjfVOIu', 'Admin', 'Système', 'ADMIN', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440011', 'manager.paris@performaassist.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSZwZlKrE42jb6q5YjfVOIu', 'Jean', 'Dupont', 'MANAGER', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440012', 'manager.lyon@performaassist.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSZwZlKrE42jb6q5YjfVOIu', 'Marie', 'Martin', 'MANAGER', '550e8400-e29b-41d4-a716-446655440002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440013', 'employee1@performaassist.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSZwZlKrE42jb6q5YjfVOIu', 'Pierre', 'Durand', 'EMPLOYEE', '550e8400-e29b-41d4-a716-446655440001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440014', 'employee2@performaassist.com', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iKXYLFSZwZlKrE42jb6q5YjfVOIu', 'Sophie', 'Bernard', 'EMPLOYEE', '550e8400-e29b-41d4-a716-446655440002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertion des modèles de checklist de test
INSERT INTO checklist_templates (id, name, description, category, created_by, is_active, created_at, updated_at) VALUES
('550e8400-e29b-41d4-a716-446655440020', 'Checklist Sécurité Quotidienne', 'Vérifications de sécurité à effectuer quotidiennement', 'Sécurité', '550e8400-e29b-41d4-a716-446655440011', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440021', 'Contrôle Qualité Production', 'Points de contrôle qualité pour la production', 'Qualité', '550e8400-e29b-41d4-a716-446655440011', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('550e8400-e29b-41d4-a716-446655440022', 'Audit 5S', 'Checklist pour l''audit 5S des postes de travail', '5S', '550e8400-e29b-41d4-a716-446655440012', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Insertion des éléments de checklist
INSERT INTO checklist_items (id, template_id, order_index, title, description, is_mandatory, scoring_type, max_score) VALUES
-- Checklist Sécurité
('550e8400-e29b-41d4-a716-446655440030', '550e8400-e29b-41d4-a716-446655440020', 1, 'Vérification des EPI', 'Tous les équipements de protection individuelle sont-ils disponibles et en bon état ?', true, 'BINARY', 1),
('550e8400-e29b-41d4-a716-446655440031', '550e8400-e29b-41d4-a716-446655440020', 2, 'État des extincteurs', 'Les extincteurs sont-ils accessibles et vérifiés ?', true, 'BINARY', 1),
('550e8400-e29b-41d4-a716-446655440032', '550e8400-e29b-41d4-a716-446655440020', 3, 'Signalisation sécurité', 'La signalisation de sécurité est-elle visible et complète ?', true, 'BINARY', 1),
('550e8400-e29b-41d4-a716-446655440033', '550e8400-e29b-41d4-a716-446655440020', 4, 'Propreté des allées', 'Les allées de circulation sont-elles dégagées et propres ?', false, 'SCALE', 5),

-- Checklist Qualité
('550e8400-e29b-41d4-a716-446655440040', '550e8400-e29b-41d4-a716-446655440021', 1, 'Conformité des matières premières', 'Les matières premières respectent-elles les spécifications ?', true, 'BINARY', 1),
('550e8400-e29b-41d4-a716-446655440041', '550e8400-e29b-41d4-a716-446655440021', 2, 'Étalonnage des instruments', 'Les instruments de mesure sont-ils étalonnés ?', true, 'BINARY', 1),
('550e8400-e29b-41d4-a716-446655440042', '550e8400-e29b-41d4-a716-446655440021', 3, 'Contrôle dimensionnel', 'Les dimensions des pièces sont-elles conformes ?', true, 'PERCENTAGE', 100),

-- Checklist 5S
('550e8400-e29b-41d4-a716-446655440050', '550e8400-e29b-41d4-a716-446655440022', 1, 'Seiri (Débarrasser)', 'Le poste ne contient que le nécessaire', true, 'SCALE', 5),
('550e8400-e29b-41d4-a716-446655440051', '550e8400-e29b-41d4-a716-446655440022', 2, 'Seiton (Ranger)', 'Chaque chose a sa place et est à sa place', true, 'SCALE', 5),
('550e8400-e29b-41d4-a716-446655440052', '550e8400-e29b-41d4-a716-446655440022', 3, 'Seiso (Nettoyer)', 'Le poste de travail est propre', true, 'SCALE', 5),
('550e8400-e29b-41d4-a716-446655440053', '550e8400-e29b-41d4-a716-446655440022', 4, 'Seiketsu (Standardiser)', 'Les standards sont respectés', false, 'SCALE', 5),
('550e8400-e29b-41d4-a716-446655440054', '550e8400-e29b-41d4-a716-446655440022', 5, 'Shitsuke (Maintenir)', 'La discipline est maintenue', false, 'SCALE', 5);

-- Affichage des informations de connexion
SELECT 'Données de développement insérées avec succès!' as message;
SELECT 'Comptes utilisateurs créés:' as info;
SELECT 
    email,
    first_name || ' ' || last_name as nom_complet,
    role,
    'password123' as mot_de_passe
FROM users 
ORDER BY role, email;

