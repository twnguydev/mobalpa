# Catalogue API

Le microservice de catalogue est une composante critique de l'architecture microservices d'Archidéco. Il est responsable de la gestion de tous les aspects relatifs aux produits, catégories et stocks.

#### L'API Catalogue est disponible sur le port `8081`.

## Responsabilités

- Gestion des catégories de produits, meilleures ventes, aiguillage en magasin.
- Gestion des produits (ajout, modification, suppression, catégories et sous-catégories).
- Gestion des stocks et des disponibilités.

## Modélisation de la base de données

Nous avons choisi MongoDB comme base de données pour plusieurs raisons :

- **Scalabilité** : MongoDB est conçu pour être scalable, ce qui permet de gérer efficacement une grande quantité de données et d'augmenter les capacités de stockage et de traitement à mesure que les besoins de l'application croissent.
- **Flexibilité du schéma** : MongoDB utilise un modèle de données flexible basé sur des documents, ce qui permet de stocker des données semi-structurées sans avoir à définir un schéma strict à l'avance. Cela est particulièrement utile pour gérer des données hétérogènes et évolutives.
- **Performance** : MongoDB offre des performances élevées pour les opérations de lecture et d'écriture, grâce à des fonctionnalités telles que l'indexation et le partitionnement horizontal (sharding).
- **Écosystème riche** : MongoDB dispose d'un écosystème riche comprenant des outils de gestion, de surveillance et de visualisation des données, ainsi que des bibliothèques et des frameworks pour faciliter le développement.
- **Communauté et support** : MongoDB bénéficie d'une large communauté de développeurs et d'un support professionnel, ce qui garantit des ressources abondantes et une assistance fiable en cas de besoin.

L'utilisation de MongoDB nous permet de répondre aux exigences de flexibilité, de scalabilité et de performance de la plateforme e-commerce **Archidéco**, tout en simplifiant la gestion et l'évolution des données.

![Archidéco Data Database](./assets/db_catalogue.png)

## Endpoints nécessaires

- `GET /api/catalogue/categories`: Récupération des catégories de produits.
- `GET /api/catalogue/best-sellers`: Récupération des meilleures ventes.
- `GET /api/catalogue/store/{id}`: Aiguillage en magasin.
- `GET /api/catalogue/products`: Récupération des produits.
- `GET /api/catalogue/products/{category_id}`: Récupération des produits d'une catégorie ou d'une sous-catégorie.
- `POST /api/catalogue/products`: Ajout d'un nouveau produit.
- `PATCH /api/catalogue/products/{id}`: Mise à jour d'un produit.
- `DELETE /api/catalogue/products/{id}`: Suppression d'un produit.
- `GET /api/catalogue/products/categories`: Récupération d'une catégorie ou d'une sous-catégorie.
- `POST /api/catalogue/products/categories`: Ajout d'une catégorie ou d'une sous-catégorie.
- `PATCH /api/catalogue/products/categories/{id}`: Mise à jour d'une catégorie ou d'une sous-catégorie.
- `DELETE /api/catalogue/products/categories/{id}`: Suppression d'une catégorie ou d'une sous-catégorie.

## Exemple d'utilisation

### Ajout d'un nouveau produit
```http
POST /api/catalogue/products
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Canapé Moderne",
  "description": "Un canapé moderne et confortable",
  "price": 599.99,
  "stock": 10,
  "category": "Meubles"
}
```

### Mise à jour d'un produit
```http
PATCH /api/catalogue/products/12345
Authorization: Bearer <token>
Content-Type: application/json

{
  "price": 549.99,
  "stock": 15
}
```

### Suppression d'un produit
```http
DELETE /api/catalogue/products/12345
Authorization: Bearer <token>
Content-Type: application/json
```