import { IOrder } from '@interfaces/order.interface';
import { IWishlist } from '@interfaces/wishlist.interface';
import { IPayment } from '@interfaces/payment.interface';

export interface IUser {
  uuid: string;
  firstname: string;
  lastname: string;
  email: string;
  password?: string;
  confirmPassword?: string | undefined;
  phoneNumber: string;
  birthdate: string;
  address?: string | null;
  zipcode?: string | null;
  city?: string | null;
  token?: string | null;
  active: boolean;
  createdAt?: string;
  updatedAt?: string | null;
  roles?: IRole[];
  wishlist?: IWishlist | null;
  payments?: IPayment[];
  orders?: IOrder[];
}

export interface IVisitor {
  uuid?: string;
  firstname: string;
  lastname: string;
  email: string;
  phoneNumber: string;
  address?: string | null;
  zipcode?: string | null;
  city?: string | null;
  active: boolean;
  createdAt?: string;
  payments?: IPayment[];
  orders?: IOrder[];
}

export interface IRole {
  id: string;
  name: string;
}