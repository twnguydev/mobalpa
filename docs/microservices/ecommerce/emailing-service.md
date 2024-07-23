### Emailing Service

#### Responsabilités :
- Gestion des e-mails d’incitation à l’achat, toasts d’incitation, envoi de newsletters et de magazines, envoi des bons de commande et factures.

#### Endpoints nécessaires :
- `POST /api/emails/send` : Envoi d’un e-mail.
- `POST /api/emails/newsletter` : Abonnement à la newsletter.
- `DELETE /api/emails/newsletter`: Suppression d'un abonnement à la newsletter.
- `GET /api/emails/{id}/status` : Suivi de l’état d’un e-mail envoyé.

#### Exemple d'utilisation :

##### Envoi d'un e-mail
```http
POST /api/emails/send
Authorization: Bearer <token>
Content-Type: application/json

{
  "to": "customer@example.com",
  "subject": "Votre commande a été expédiée",
  "body": "Votre commande #54321 a été expédiée."
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "emailId": "abc123",
  "status": "Sent"
}
```

##### Abonnement à la newsletter
```http
POST /api/emails/newsletter
Authorization: Bearer <token>
Content-Type: application/json

{
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
  "subscriptionId": "newsletter123",
  "status": "Subscribed"
}
```

##### Suppression d'un abonnement à la newsletter
```http
DELETE /api/emails/newsletter
Authorization: Bearer <token>
Content-Type: application/json

{
  "userUuid": "a4801d5a-cbac-417e-8d99-d690b3832f19"
}

HTTP/1.1 204 No Content
```

##### Suivi de l'état d'un e-mail envoyé
```http
GET /api/emails/abc123/status
Authorization: Bearer <token>
Content-Type: application/json

HTTP/1.1 200 OK
Content-Type: application/json

{
  "emailId": "abc123",
  "status": "Opened"
}
```