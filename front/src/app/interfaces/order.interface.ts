import { IProduct } from './product.interface';

export interface IOrder {
  uuid: string;
  userId: string;
  items: IOrderItem[];
  deliveryMethod: string;
  vat: number;
  reduction: number;
  taxDelivery: number;
  warranty: string;
  totalHt: number;
  totalTtc: number;
  createdAt: string;
  status: string;
  deliveryFees: number;
  deliveryAddress: string;
  paymentId: string;
  invoice: Blob | null;
}

export interface IOrderItem {
  productUuid: string;
  quantity: number;
  product?: IProduct | null;
}

export interface ICouponCodeResponse {
  discountRate: number;
  discountType: "PERCENTAGE" | "AMOUNT";
}
