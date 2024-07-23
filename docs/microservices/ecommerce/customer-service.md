### Customer Service

#### Responsabilités :
- Gestion des garanties et du service après-vente.
- Reprise des meubles remplacés.

#### Endpoints nécessaires :
- `POST /api/support/ticket` : Création d’un ticket de support.
- `GET /api/support/ticket/{id}` : Récupération des détails d’un ticket de support.
- `POST /api/support/ticket/{id}/resolve` : Résolution d’un ticket de support.
- `POST /api/support/return` : Demande de reprise de meuble.

#### Exemple d'utilisation :

##### Création d'un ticket de support
```http
POST /api/support/ticket
Authorization: Bearer <token>
Content-Type: application/json

{
  "userId": "1",
  "issue": "Problème avec le produit reçu"
}
```

##### Résolution d'un ticket de support
```http
POST /api/support/ticket/{id}/resolve
Authorization: Bearer <token>
Content-Type: application/json

{
  "resolution": "Produit remplacé"
}
```