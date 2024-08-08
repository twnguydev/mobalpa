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
    "to": "nathanmatounga@gmail.com",
    "subject": "Hello",
    "body": "Coucou copain de moi!!",
    "status": "SENT"
}

HTTP/1.1 200 OK
Content-Type: application/json


  Email sent

```

##### Abonnement à la newsletter
```http
POST /api/emails/newsletter
Authorization: Bearer <token>
Content-Type: application/json

{
    "emailUser": "nathanmatounga@gmail.com"
}

HTTP/1.1 200 OK
Content-Type: application/json

Newsletter saved
```

##### Suppression d'un abonnement à la newsletter
```http
DELETE /api/emails/newsletter
Authorization: Bearer <token>
Content-Type: application/json

{
    "emailUser": "nathanmatounga@gmail.com"
}

HTTP/1.1 200 ok

Newsletter supprimée
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
