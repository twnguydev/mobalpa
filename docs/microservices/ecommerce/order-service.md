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
  "userId": "1",
  "items": [
    {
      "productId": "101",
      "quantity": 2
    }
  ],
  "vat": 59.99,
  "reduction": "10.50",
  "total": 199.98
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
```

##### Application d'un coupon de réduction
```http
POST /api/orders/{id}/apply-coupon
Authorization: Bearer <token>
Content-Type: application/json

{
  "couponCode": "SUMMER2023"
}
```