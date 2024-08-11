# Delivery API

Le microservice de livraison est une composante essentielle de l'architecture microservices d'Archidéco. Il est responsable de la gestion de toutes les opérations liées à la livraison des produits commandés par les clients.

#### L'API Catalogue est disponible sur le port `8082`.

## Responsabilités

- Gestion des livraisons.
- Intégration avec les API de livraison (La Poste, Chronopost, etc.).
- Suivi et mise à jour des statuts de livraison.

## Endpoints nécessaires

- `POST /api/deliveries`: Création d'une nouvelle livraison.
- `GET /api/deliveries/{id}`: Récupération des détails d’une livraison.
- `PATCH /api/deliveries/{id}`: Mise à jour du statut d'une livraison.
- `GET /api/deliveries/{id}/track`: Récupération du suivi d'une livraison.
- `GET /api/deliveries/prices`: Récupération des tarifs de livraison.

## Exemple d'utilisation

### Création d'une nouvelle livraison
```http
POST /api/deliveries
X-API-KEY: <token>
Content-Type: application/json

{
  "orderId": "12345",
  "address": "10 rue de la Paix, 75002 Paris, France",
  "deliveryMethod": "Chronopost"
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "deliveryId": "67890",
  "orderId": "12345",
  "address": "10 rue de la Paix, 75002 Paris, France",
  "deliveryMethod": "Chronopost",
  "status": "Pending",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z"
}
```

### Récupération des détails d'une livraison
```http
GET /api/deliveries/67890
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "deliveryId": "67890",
  "orderId": "12345",
  "address": "10 rue de la Paix, 75002 Paris, France",
  "deliveryMethod": "Chronopost",
  "status": "Pending",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z"
}
```

### Mise à jour du statut d'une livraison
```http
PATCH /api/deliveries/67890
X-API-KEY: <token>
Content-Type: application/json

{
  "status": "Shipped"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "deliveryId": "67890",
  "orderId": "12345",
  "address": "10 rue de la Paix, 75002 Paris, France",
  "deliveryMethod": "Chronopost",
  "status": "Shipped",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T12:00:00Z"
}
```

### Suivi d'une livraison
```http
GET /api/deliveries/67890/track
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "deliveryId": "67890",
  "orderId": "12345",
  "deliveryMethod": "Chronopost",
  "status": "Shipped",
  "trackingDetails": {
    "currentLocation": "Centre de tri, Paris",
    "estimatedDeliveryDate": "2023-07-24T10:00:00Z",
    "history": [
      {
        "location": "Centre de tri, Paris",
        "status": "Shipped",
        "timestamp": "2023-07-22T12:00:00Z"
      },
      {
        "location": "Entrepôt, Paris",
        "status": "Prepared",
        "timestamp": "2023-07-22T11:00:00Z"
      }
    ]
  }
}
```

### Récupération des tarifs de livraison
```http
GET /api/deliveries/prices
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "methods": [
    {
      "name": "Chronopost",
      "price": 5.60
    },
    {
      "name": "La Poste",
      "price": 4.20
    }
  ]
}
```