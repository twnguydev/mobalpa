# Delivery API

Le microservice de livraison est une composante essentielle de l'architecture microservices d'Archidéco. Il est responsable de la gestion de toutes les opérations liées à la livraison des produits commandés par les clients.

#### L'API Catalogue est disponible sur le port `8082`.

## Responsabilités

- Gestion des livraisons.
- Intégration potentielle avec des API de livraison externes (La Poste, Chronopost, etc.).
- Suivi et mise à jour des statuts de livraison.

## Endpoints nécessaires

- `POST /api/delivery`: Création d'une nouvelle livraison.
- `GET /api/delivery/{id}`: Récupération des détails d’une livraison.
- `PATCH /api/delivery/{id}`: Mise à jour du statut d'une livraison.
- `GET /api/delivery/depot`: Récupération des dépôts de livraison.
- `GET /api/delivery/depot/{depotName}`: Récupération des informations d'un dépôt de livraison.
- `DELETE /api/delivery/{deliveryNumber}`: Suppression d'une livraison.

## Exemple d'utilisation

### Création d'une nouvelle livraison
```http
POST /api/delivery
X-API-KEY: <token>
Content-Type: application/json

{
  "parcelItems": [
    {
      "description": "Blender",
      "productId": "BLD123456",
      "properties": {
        "color": "Red",
        "power": "500W"
      },
      "quantity": 1,
      "value": 49.99,
      "weight": 2.5,
      "width": 10.0,
      "height": 20.0
    },
    {
      "description": "Microwave Oven",
      "productId": "MWO987654",
      "properties": {
        "color": "White",
        "capacity": "20L"
      },
      "quantity": 1,
      "value": 99.99,
      "weight": 10.5,
      "width": 50.0,
      "height": 30.0
    }
  ],
  "shippingMethodCheckoutName": "Standard Delivery",
  "senderAddress": "Warehouse 1",
  "totalInsuredValue": 150,
  "isReturn": false
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "trackingNumber": "TRK1234567890"
}
```

### Récupération des détails d'une livraison
```http
GET /api/delivery/67890
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
PATCH /api/delivery/TRACKING123456
X-API-KEY: <token>
Content-Type: application/json

{
  "status": "Shipped"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "uuid": "e8e8f8f8-f8f8-4f8f-b8b8-123456789abc",
  "name": "John Doe",
  "companyName": "Mobalpa",
  "email": "john.doe@example.com",
  "telephone": "+33612345678",
  "address": "1, rue de la Paix",
  "houseNumber": "1",
  "address2": "Bâtiment B",
  "city": "Marseille",
  "country": "FR",
  "postalCode": "13001",
  "weight": 3.38,
  "width": 3.38,
  "height": 3.38,
  "totalOrderValue": 896.87,
  "totalOrderValueCurrency": "EUR",
  "shippingMethodCheckoutName": "Battery WarehouseX DHL",
  "senderAddress": "1",
  "quantity": 1,
  "totalInsuredValue": 0,
  "isReturn": false,
  "status": "Shipped",
  "shipment": {
    "orderUuid": "12345678-1234-1234-1234-123456789abc",
    "name": "DHL Parcel Connect 2-5kg to ParcelShop",
    "deliveryNumber": "TRACKING123456"
  },
  "parcelItems": [
    {
      "uuid": "12345678-1234-1234-1234-123456789def",
      "description": "T-Shirt",
      "productId": "898678671",
      "properties": {
        "color": "Blue",
      },
      "quantity": 2,
      "value": 19.95,
      "weight": 1.69,
      "width": 1.69,
      "height": 1.69
    },
    {
      "uuid": "12345678-1234-1234-1234-123456789ghi",
      "description": "Laptop",
      "productId": "5756464758",
      "properties": {
        "color": "Black",
      },
      "quantity": 1,
      "value": 876.97,
      "weight": 1.69,
      "width": 1.69,
      "height": 1.69
    }
  ]
}
```

### Suppression d'une livraison
```http
DELETE /api/delivery/TRACKING123456
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 204 No Content
```

### Récupération des dépôts de livraison
```http
GET /api/delivery/depot
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "Chronopost": {
    "price": 5.6,
    "address": "Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris"
  },
  "La Poste": {
    "price": 4.2,
    "address": "La Poste Depot, 25 Avenue de la République, 75011 Paris"
  },
  "Mobalpa Centrale": {
    "price": 0.0,
    "address": "Mobalpa Centrale, 10 Rue du Commerce, 69002 Lyon"
  }
}
```

### Récupération des informations d'un dépôt de livraison
```http
GET /api/delivery/depot/Chronopost
X-API-KEY: <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "price": 5.6,
  "address": "Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris"
}
```