export interface IPayment {
  uuid: string;
  userUuid: string;
  cardNumber?: string;
  expirationDate?: string;
  cvv?: string;
  cardHolder: string;
  paypalEmail?: string | null;
  paymentMethod: 'CREDIT_CARD' | 'PAYPAL';
  createdAt?: string;
}