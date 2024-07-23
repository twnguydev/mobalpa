### Emailing Service

#### Responsabilités :
- Gestion des e-mails d’incitation à l’achat, toasts d’incitation, envoi de newsletters et de magazines, envoi des bons de commande et factures.

#### Endpoints nécessaires :
- `POST /api/emails/send` : Envoi d’un e-mail.
- `POST /api/emails/newsletter` : Abonnement à la newsletter.
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
  "body": "Votre commande #12345 a été expédiée."
}
```