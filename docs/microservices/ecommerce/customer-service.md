### Customer Service

#### Responsabilités :
- Gestion des garanties et du service après-vente.
- Reprise des meubles remplacés.

#### Endpoints nécessaires :
- `POST /api/support/ticket` : Création d’un ticket de support.
- `GET /api/support/ticket/{id}` : Récupération des détails d’un ticket de support.
- `PATCH /api/support/ticket/{id}/resolve` : Résolution d’un ticket de support.
- `POST /api/support/return` : Demande de reprise de meuble.

#### Exemple d'utilisation :

##### Création d'un ticket de support
```http
POST /api/support/ticket
Authorization: Bearer <token>
Content-Type: application/json

{
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "type": "product",
  "name": "Problème avec le produit",
  "issue": "Le produit est défectueux"
}

HTTP/1.1 201 Created
Content-Type: application/json

{
  "uuid": "abcdef12-3456-7890-abcd-ef1234567890",
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "type": "product",
  "name": "Problème avec le produit",
  "issue": "Le produit est défectueux",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z",
}
```

##### Récupération des détails d'un ticket de support
```http
GET /api/support/ticket/abcdef12-3456-7890-abcd-ef1234567890
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "uuid": "abcdef12-3456-7890-abcd-ef1234567890",
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "type": "product",
  "name": "Problème avec le produit",
  "issue": "Le produit est défectueux",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z",
  "closedAt": null,
  "responderUuid": null,
  "resolution": null
}
```

##### Résolution d'un ticket de support
```http
PATCH /api/support/ticket/abcdef12-3456-7890-abcd-ef1234567890/resolve
Authorization: Bearer <token>
Content-Type: application/json

{
  "resolution": "Produit remplacé"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "uuid": "abcdef12-3456-7890-abcd-ef1234567890",
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19",
  "type": "product",
  "name": "Problème avec le produit",
  "issue": "Le produit est défectueux",
  "createdAt": "2023-07-22T10:00:00Z",
  "updatedAt": "2023-07-22T10:00:00Z",
  "closedAt": "2023-07-23T10:00:00Z",
  "responderUuid": "123e4567-e89b-12d3-a456-426614174000",
  "resolution": "Produit remplacé"
}
```