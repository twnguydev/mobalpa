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

#### Exemple d'utilisation :

##### Création d'une nouvelle commande
```http
POST /api/orders
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "101",
      "quantity": 2
    }
  ],
  "deliveryMethod": "Chronopost",
  "reduction": 10,
  "totalHt": 599.99,
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "orderId": "54321",
  "userId": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "101",
      "quantity": 2
    }
  ],
  "vat": 59.90,
  "reduction": 10,
  "taxDelivery": 5.60,
  "warranty": "2030-07-22T10:00:00Z",
  "totalHt": 524.49,
  "totalTtc": 599.99
}
```

##### Récupération des commandes
```http
GET /api/orders
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "orderId": "54321",
    "userId": "a4801d5a-cbac-417e-8d99-d690b3832f19",
    "items": [
      {
        "productId": "101",
        "quantity": 2
      }
    ],
    "vat": 59.90,
    "reduction": 10,
    "taxDelivery": 5.60,
    "warranty": "2030-07-22T10:00:00Z",
    "totalHt": 524.49,
    "totalTtc": 599.99
  }
]
```

##### Récupération d'une commande
```http
GET /api/orders/54321
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "orderId": "54321",
  "userId": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "items": [
    {
      "productId": "101",
      "quantity": 2
    }
  ],
  "vat": 59.90,
  "reduction": 10,
  "taxDelivery": 5.60,
  "warranty": "2030-07-22T10:00:00Z",
  "totalHt": 524.49,
  "totalTtc": 599.99
}

```

##### Traitement du paiement par Stripe
```http
POST /api/orders/{id}/payment
Authorization: Bearer <token>
Content-Type: application/json

{
  "paymentMethod": "stripe",
  "paymentDetails": {
    "cardNumber": "4242424242424242",
    "expirationDate": "12/25",
    "cvv": "123"
  }
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "status": "Accepted"
}
```

##### Application d'un coupon de réduction
```http
POST /api/orders/{id}/apply-coupon
Authorization: Bearer <token>
Content-Type: application/json

{
  "couponCode": "SUMMER2023"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "discountRate": 20
}
```