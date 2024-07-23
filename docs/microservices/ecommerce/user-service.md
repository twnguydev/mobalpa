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

#### Exemple d'utilisation :

##### Inscription d'un nouvel utilisateur
```http
POST /api/users/register
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123",
  "email": "john@example.com"
}
```

##### Connexion d'un utilisateur
```http
POST /api/users/login
Content-Type: application/json

{
  "username": "john_doe",
  "password": "password123"
}
```

##### Récupération de la wishlist
```http
GET /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19/wishlist
Authorization: Bearer <token>
Content-Type: application/json
```