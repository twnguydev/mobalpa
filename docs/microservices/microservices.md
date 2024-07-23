# Développement des microservices pour la société Archidéco

Pour répondre aux exigences de l'architecture du projet Archideco, nous allons détailler les microservices en mettant en avant leurs responsabilités, interactions, choix technologiques et exemples de mise en œuvre.

Cette architecture microservices permet une gestion modulaire et évolutive de toutes les fonctionnalités nécessaires au fonctionnement de la plateforme e-commerce Archidéco. Chaque microservice est responsable d'une partie spécifique du système, ce qui facilite le développement, le déploiement et la maintenance. Les intégrations avec des API externes et l'utilisation de technologies modernes garantissent une performance optimale et une excellente expérience utilisateur.

Voici une description approfondie des microservices :

## Microservices internes (API principale e-commerce)

### User Service

#### Responsabilités :
- Gestion des comptes utilisateurs (Customer, Admin, Super Admin).
- Stockage des données utilisateurs (visites, adresse IP, etc).
- Gestion des listes de souhaits.
- Authentification et autorisations (JWT tokens).

#### Endpoints nécessaires :
- `POST /api/users/register`: Inscription d'un nouvel utilisateur.
- `POST /api/users/login`: Connexion d'un utilisateur.
- `PATCH /api/users/{id}`: Modification d'un utilisateur.
- `GET /api/users/{id}`: Récupération des données d'un utilisateur.
- `GET /api/users/{id}/wishlist`: Récupération de la liste de souhaits de l'utilisateur.
- `GET /api/users/{id}/orders`: Récupération des commandes de l'utilisateur.

#### Package :
`com.mobalpa.api.users`

### Order Service

#### Responsabilités :
- Gestion des commandes.
- Intégration avec Stripe API.
- Gestion des coupons de promotion et du système de points.

#### Endpoints nécessaires :
- `POST /api/orders`: Création d'une nouvelle commande.
- `GET /api/orders`: Récupération des commandes.
- `GET /api/orders/{id}`: Récupération d'une commande.
- `POST /api/orders/{id}/payment`: Traitement du paiement par Stripe.
- `POST /api/orders/{id}/apply-coupon`: Application d'un coupon de réduction.

#### Package :
`com.mobalpa.api.orders`

### Customer Service

#### Responsabilités :
- Gestion des garanties et du service après-vente.
- Reprise des meubles remplacés.

#### Endpoints nécessaires :
- `POST /api/support/ticket` : Création d’un ticket de support.
- `GET /api/support/ticket/{id}` : Récupération des détails d’un ticket de support.
- `POST /api/support/ticket/{id}/resolve` : Résolution d’un ticket de support.
- `POST /api/support/return` : Demande de reprise de meuble.

#### Package :
`com.mobalpa.api.customers`

### Emailing Service

#### Responsabilités :
- Gestion des e-mails d’incitation à l’achat, toasts d’incitation, envoi de newsletters et de magazines, envoi des bons de commande et factures.

#### Endpoints nécessaires :
- `POST /api/emails/send` : Envoi d’un e-mail.
- `POST /api/emails/newsletter` : Abonnement à la newsletter.
- `GET /api/emails/{id}/status` : Suivi de l’état d’un e-mail envoyé.

#### Package :
`com.mobalpa.api.email`

### Analytics Service

#### Responsabilités :
- Collecte des données utilisateur (produits vus, ajoutés, statistiques).
- Export des données chiffrées (CA, marges, livraisons) en Excel.

#### Endpoints nécessaires :
- `GET /api/analytics/user-data` : Récupération des données utilisateur.
- `GET /api/analytics/export` : Export des données en Excel.

#### Package :
`com.mobalpa.api.analytics`

## Microservices externes

### Catalogue Service

#### Responsabilités :
- Gestion des catégories de produits, meilleures ventes, aiguillage en magasin.
- Gestion des produits (ajout, modification, suppression, catégories et sous-catégories).
- Gestion des promotions et des périodes de déstockage.
- Gestion des stocks et des disponibilités.

#### Endpoints nécessaires :
- `GET /api/catalogue/categories` : Récupération des catégories de produits.
- `GET /api/catalogue/best-sellers` : Récupération des meilleures ventes.
- `GET /api/catalogue/store/{id}` : Aiguillage en magasin.
- `GET /api/catalogue/products`: Récupération des produits.
- `GET /api/catalogue/products/{category_id}`: Récupération des produits d'une catégorie ou d'une sous-catégorie.
- `POST /api/catalogue/products`: Ajout d'un nouveau produit.
- `PATCH /api/catalogue/products/{id}`: Mise à jour d'un produit.
- `DELETE /api/catalogue/products/{id}`: Suppression d'un produit.
- `GET /api/catalogue/products/categories`: Récupération d'une catégorie ou d'une sous-catégorie.
- `POST /api/catalogue/products/categories`: Ajout d'une catégorie ou d'une sous-catégorie.
- `PATCH /api/catalogue/products/categories/{id}`: Mise à jour d'une catégorie ou d'une sous-catégorie.
- `DELETE /api/catalogue/products/categories/{id}`: Suppression d'une catégorie ou d'une sous-catégorie.
- `GET /api/catalogue/products/campaigns`: Récupération des campagnes promotionnelles ou de déstockages.
- `POST /api/catalogue/products/campaigns`: Ajout d'un nouveau campagne promotionnelle ou de déstockage.
- `DELETE /api/catalogue/products/campaigns/{id}`: Suppression d'une campagne promotionnelle ou de déstockage.

#### Package :
`com.mobalpa.api.catalogue`

### Delivery Service

#### Responsabilités :
- Gestion des livraisons.
- Intégration avec les API de livraison (La Poste, Chronopost, etc.).

#### Endpoints nécessaires :
- `POST /api/deliveries`: Création d'une nouvelle livraison.
- `GET /api/deliveries/{id}`: Récupération des détails d’une livraison.
- `PATCH /api/deliveries/{id}`: Mise à jour du statut d'une livraison.
- `GET /api/deliveries/{id}/track`: Récupération du suivi d'une livraison.

#### Package :
`com.mobalpa.api.delivery`