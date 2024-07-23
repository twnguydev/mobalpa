### Analytics Service

#### Responsabilités :
- Collecte des données utilisateur (produits vus, ajoutés, statistiques).
- Export des données chiffrées (CA, marges, livraisons) en Excel.

#### Endpoints nécessaires :
- `GET /api/analytics/user-data` : Récupération des données utilisateur.
- `GET /api/analytics/export` : Export des données en Excel.

#### Exemple d'utilisation :

##### Récupération des données utilisateur
```http
GET /api/analytics/user-data
Authorization: Bearer <token>
Content-Type: application/json
```

##### Export des données en Excel
```http
GET /api/analytics/export
Authorization: Bearer <token>
Content-Type: application/json
```