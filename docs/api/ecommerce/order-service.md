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
  "userUuid": "73408376-18f4-45df-9bff-f386d637bcb2",
  "paymentUuid": "1ff0bb5d-fee3-4398-9da8-36c4ff521e23",
  "items": [
    {
      "productUuid": "06d59b82-b63b-4dcf-ac84-fb2d8c06931d",
      "quantity": 2
    }
  ],
  "reduction": 10.0,
  "deliveryMethod": "Chronopost",
  "deliveryAddress": "123 St. John 13001 Marseille",
  "totalHt": 599.99
}

HTTP/1.1 201 Created
Content-Type: application/json

{
    "orderUuid": "062c0c0e-a5c0-4560-9860-abe1e273f92d",
    "parcelItems": [
        {
            "productUuid": "06d59b82-b63b-4dcf-ac84-fb2d8c06931d",
            "description": "This is an example product description.",
            "quantity": 2,
            "value": 99.99,
            "weight": 1.5,
            "width": 20.0,
            "height": 10.0,
            "properties": {
                "images": "/image1.jpg, /image2.jpg",
                "brand": "BrandName",
                "colors": "Red, Blue"
            }
        }
    ],
    "shippingMethodCheckoutName": "Chronopost",
    "senderAddress": "Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris",
    "recipientAddress": "123 St. John 13001 Marseille",
    "recipientPhoneNumber": "0123456789",
    "recipientEmail": "tanguy.gibrat@epitech.eu",
    "recipientName": "Tanguy Gibrat",
    "shipment": {
        "deliveryNumber": "CHR_CkMWH2SD1hkm",
        "orderUuid": "062c0c0e-a5c0-4560-9860-abe1e273f92d",
        "name": "Chronopost",
        "address": "Chronopost Depot, 15 Rue de l'Industrie, 75012 Paris"
    }
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
        "uuid": "062c0c0e-a5c0-4560-9860-abe1e273f92d",
        "warranty": "2031-08-09",
        "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
        "reduction": 10.0,
        "deliveryFees": 5.6,
        "deliveryMethod": "Chronopost",
        "vat": 119.998,
        "totalHt": 599.99,
        "totalTtc": 715.5880000000001,
        "status": "PROCESSED",
        "items": [
            {
                "uuid": "fc1d7599-39cc-46ac-aaee-211e366a210e",
                "productUuid": "06d59b82-b63b-4dcf-ac84-fb2d8c06931d",
                "properties": {},
                "quantity": 2
            }
        ],
        "deliveryNumbers": [
            "CHR_CkMWH2SD1hkm"
        ],
        "payment": {
            "uuid": "1ff0bb5d-fee3-4398-9da8-36c4ff521e23",
            "cardNumber": null,
            "expirationDate": null,
            "cvv": null,
            "cardHolder": "John Doe",
            "paypalEmail": "john.doe@example.com",
            "paymentMethod": "PAYPAL",
            "createdAt": "2024-08-08T17:30:30"
        },
        "createdAt": "2024-08-09T15:39:26"
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
    "uuid": "062c0c0e-a5c0-4560-9860-abe1e273f92d",
    "warranty": "2031-08-09",
    "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
    "reduction": 10.0,
    "deliveryFees": 5.6,
    "deliveryMethod": "Chronopost",
    "vat": 119.998,
    "totalHt": 599.99,
    "totalTtc": 715.5880000000001,
    "status": "PROCESSED",
    "items": [
        {
            "uuid": "fc1d7599-39cc-46ac-aaee-211e366a210e",
            "productUuid": "06d59b82-b63b-4dcf-ac84-fb2d8c06931d",
            "properties": {},
            "quantity": 2
        }
    ],
    "deliveryNumbers": [
        "CHR_CkMWH2SD1hkm"
    ],
    "payment": {
        "uuid": "1ff0bb5d-fee3-4398-9da8-36c4ff521e23",
        "cardNumber": null,
        "expirationDate": null,
        "cvv": null,
        "cardHolder": "John Doe",
        "paypalEmail": "john.doe@example.com",
        "paymentMethod": "PAYPAL",
        "createdAt": "2024-08-08T17:30:30"
    },
    "createdAt": "2024-08-09T15:39:26"
}

```

##### Traitement du paiement par Stripe
```http
POST /api/orders/{id}/payment
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
    "uuid": "062c0c0e-a5c0-4560-9860-abe1e273f92d",
    "warranty": "2031-08-09",
    "deliveryAddress": "1 Place Jean Jaurès 13001 Marseille",
    "reduction": 10.0,
    "deliveryFees": 5.6,
    "deliveryMethod": "Chronopost",
    "vat": 119.998,
    "totalHt": 599.99,
    "totalTtc": 715.5880000000001,
    "status": "PROCESSED",
    "items": [
        {
            "uuid": "fc1d7599-39cc-46ac-aaee-211e366a210e",
            "productUuid": "06d59b82-b63b-4dcf-ac84-fb2d8c06931d",
            "properties": {},
            "quantity": 2
        }
    ],
    "deliveryNumbers": [
        "CHR_CkMWH2SD1hkm"
    ],
    "payment": {
        "uuid": "1ff0bb5d-fee3-4398-9da8-36c4ff521e23",
        "cardNumber": null,
        "expirationDate": null,
        "cvv": null,
        "cardHolder": "John Doe",
        "paypalEmail": "john.doe@example.com",
        "paymentMethod": "PAYPAL",
        "createdAt": "2024-08-08T17:30:30"
    },
    "createdAt": "2024-08-09T15:39:26"
}
```

##### Application d'un coupon de réduction
```http
POST /api/orders/{userUuid}/apply-coupon
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