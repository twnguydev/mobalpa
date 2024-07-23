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
- `PATCH /api/users/{id}/wishlist`: Modification de la liste de souhaits de l'utilisateur.
- `GET /api/users/{id}/orders`: Récupération des commandes de l'utilisateur.

#### Exemple d'utilisation :

##### Inscription d'un nouvel utilisateur
```http
POST /api/users/register
Content-Type: application/json

{
  "firstname": "John",
  "lastname": "Doe",
  "password": "password123",
  "email": "john@example.com"
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "firstname": "John",
  "lastname": "Doe",
  "email": "john@example.com",
  "createdAt": "2023-07-22T10:00:00Z"
}
```

##### Connexion d'un utilisateur
```http
POST /api/users/login
Content-Type: application/json

{
  "email": "john@example.com",
  "password": "password123"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "token": "<jwt_token>"
}
```

##### Modification d'un utilisateur
```http
PATCH /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19
Authorization: Bearer <token>
Content-Type: application/json

{
  "firstname": "Marie",
  "lastname": "Moe",
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "id": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "firstname": "Marie",
  "lastname": "Moe",
  "email": "john@example.com",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z"
}
```

##### Récupération de la wishlist
```http
GET /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19/wishlist
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "user_uuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "12345",
      "productName": "Canapé Moderne",
      "quantity": 1
    }
  ]
}
```

##### Modification de la wishlist
###### Ajout d'un item
```http
PATCH /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19/wishlist
Authorization: Bearer <token>
Content-Type: application/json

{
  "action": "add",
  "item": {
    "productId": "67890",
    "productName": "Table Basse",
    "quantity": 1
  }
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "user_uuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "12345",
      "productName": "Canapé Moderne",
      "quantity": 1
    },
    {
      "productId": "67890",
      "productName": "Table Basse",
      "quantity": 1
    }
  ]
}
```

###### Suppression d'un item
```http
PATCH /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19/wishlist
Authorization: Bearer <token>
Content-Type: application/json

{
  "action": "remove",
  "item": {
    "productId": "12345"
  }
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "user_uuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "67890",
      "productName": "Table Basse",
      "quantity": 1
    }
  ]
}
```

##### Récupération des commandes de l'utilisateur
```http
GET /api/users/a4801d5a-cbac-417e-8d99-d690b3832f19/orders
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "orderId": "54321",
    "items": [
      {
        "productId": "12345",
        "productName": "Canapé Moderne",
        "quantity": 1
      }
    ],
    "vat": 59.90,
    "reduction": 10,
    "taxDelivery": 5.60,
    "warranty": "2030-07-22T10:00:00Z",
    "totalHt": 599.99,
    "createdAt": "2023-07-22T10:00:00Z"
  }
]
```