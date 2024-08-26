import { IProduct } from './product.interface';

export interface IOrder {
  uuid?: string | undefined;
  userUuid: string | undefined;
  items: IOrderItem[];
  deliveryMethod?: string;
  vat?: number;
  reduction: number;
  taxDelivery?: number;
  warranty?: string;
  totalHt: number;
  totalTtc?: number;
  createdAt?: string;
  status?: string;
  deliveryFees?: number;
  deliveryAddress: string;
  paymentUuid?: string;
  invoice?: Blob | null;
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

export interface IDeliveryMethod {
  name?: string;
  price: number;
  address: string;
}

export interface IDeliveryResponse {
  [key: string]: IDeliveryMethod;
}
