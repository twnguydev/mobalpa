### Commerce Service

#### Endpoints nécessaires :
- `GET /api/promotions`: Récupération des campagnes promotionnelles ou de déstockages.
- `POST /api/promotions`: Ajout d'une nouvelle campagne promotionnelle ou de déstockage.
- `DELETE /api/promotions/{id}`: Suppression d'une campagne promotionnelle ou de déstockage.

#### Exemple d'utilisation :

##### Gestion d'une campagne de déstockage

```http
POST /api/promotions
Authorization: Bearer <token>
Content-Type: application/json

{
  "name": "Soldes d'été",
  "startDate": "2023-06-01",
  "endDate": "2023-06-30",
  "discountRate": 20
}
```