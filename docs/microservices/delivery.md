# Delivery API

Le microservice de livraison est une composante essentielle de l'architecture microservices d'Archidéco. Il est responsable de la gestion de toutes les opérations liées à la livraison des produits commandés par les clients.

## Responsabilités

- Gestion des livraisons.
- Intégration avec les API de livraison (La Poste, Chronopost, etc.).
- Suivi et mise à jour des statuts de livraison.

## Endpoints nécessaires

- `POST /api/deliveries`: Création d'une nouvelle livraison.
- `GET /api/deliveries/{id}`: Récupération des détails d’une livraison.
- `PATCH /api/deliveries/{id}`: Mise à jour du statut d'une livraison.
- `GET /api/deliveries/{id}/track`: Récupération du suivi d'une livraison.

## Exemple d'utilisation

### Création d'une nouvelle livraison

```http
POST /api/deliveries
Content-Type: application/json

{
  "orderId": "12345",
  "address": "10 rue de la Paix, 75002 Paris, France",
  "deliveryMethod": "Chronopost"
}
```

### Récupération des détails d'une livraison

```http
GET /api/deliveries/67890
Content-Type: application/json
```

### Mise à jour du statut d'une livraison

```http
PATCH /api/deliveries/67890
Content-Type: application/json

{
  "status": "Shipped"
}
```

### Suivi d'une livraison

```http
GET /api/deliveries/67890/track
Content-Type: application/json
```