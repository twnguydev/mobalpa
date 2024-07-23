### Commerce Service

#### Endpoints nécessaires :
- `GET /api/promotions`: Récupération des campagnes promotionnelles ou de déstockages.
- `POST /api/promotions`: Ajout d'une nouvelle campagne promotionnelle ou de déstockage.
- `DELETE /api/promotions/{id}`: Suppression d'une campagne promotionnelle ou de déstockage.

#### Exemple d'utilisation :

##### Récupération des campagnes promotionnelles ou de déstockages
```http
GET /api/promotions
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
  {
    "id": 1,
    "type": "coupon_code",
    "name": "SUMMER2023",
    "discountRate": 20,
    "targetUuid": null,
    "dateStart": "2023-06-01T00:00:00Z",
    "dateEnd": "2023-06-30T23:59:59Z",
    "createdAt": "2023-05-01T10:00:00Z"
  }
]
```

##### Ajout d'une nouvelle campagne promotionnelle ou de déstockage
```http
POST /api/promotions
Authorization: Bearer <token>
Content-Type: application/json

{
  "type": "coupon_code",
  "name": "SUMMER2023",
  "discountRate": 20,
  "targetUuid": null,
  "dateStart": "2023-06-01T00:00:00Z",
  "dateEnd": "2023-06-30T23:59:59Z"
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "type": "coupon_code",
  "name": "SUMMER2023",
  "discountRate": 20,
  "targetUuid": null,
  "dateStart": "2023-06-01T00:00:00Z",
  "dateEnd": "2023-06-30T23:59:59Z",
  "createdAt": "2023-05-01T10:00:00Z"
}
```

##### Gestion d'une campagne de déstockage
```http
POST /api/promotions
Authorization: Bearer <token>
Content-Type: application/json

{
  "type": "destock_subcategory",
  "name": "Soldes d'été",
  "discountRate": 20,
  "targetUuid": "874367",
  "dateStart": "2023-06-01T00:00:00Z",
  "dateEnd": "2023-06-30T23:59:59Z",
}
```

##### Suppression d'une campagne promotionnelle ou de déstockage
```http
DELETE /api/promotions/1
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 204 No Content
```