export interface IOrder {
  orderId: string;
  userId: string;
  items: IOrderItem[];
  deliveryMethod: string;
  vat: number;
  reduction: number;
  taxDelivery: number;
  warranty: string;
  totalHt: number;
  totalTtc: number;
}

export interface IOrderItem {
  productUuid: string;
  quantity: number;
}
