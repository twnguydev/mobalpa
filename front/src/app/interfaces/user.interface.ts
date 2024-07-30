import { IOrder } from '@interfaces/order.interface';

export interface IUser {
  uuid: string;
  firstname: string;
  lastname: string;
  email: string;
  password: string;
  confirmPassword?: string | undefined;
  phoneNumber: string;
  birthdate: string;
  address?: string | null;
  zipcode?: string | null;
  city?: string | null;
  token?: string | null;
  active: boolean;
  createdAt: string;
  updatedAt?: string | null;
  roles: IRole[];
  wishlist?: IWishlist | null;
  payments: IPayment[];
  orders: IOrder[];
}

export interface IRole {
  id: string;
  name: string;
}

export interface IWishlist {
  id: string;
  items: IItem[];
}

export interface IItem {
  id: string;
  name: string;
  price: number;
}

export interface IPayment {
  id: string;
  amount: number;
  date: string;
}
