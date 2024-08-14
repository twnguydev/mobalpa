import { ICategory, ISubcategory } from '@interfaces/category.interface';

export interface IProduct {
  uuid: string;
  name: string;
  description: string;
  uri: string;
  price: number;
  stock: number;
  estimatedDelivery: string;
  weight: number;
  height: number;
  width: number;
  category: ICategory;
  subcategory: ISubcategory;
  brand: IBrand;
  colors: IColor[];
  images: IImage[];
  stores: IStore[];
}

export interface IBrand {
  uuid: string;
  name: string;
}

export interface IColor {
  uuid: string;
  name: string;
}

export interface IImage {
  uuid: string;
  uri: string;
  color: IColor;
}

export interface IStore {
  uuid: string;
  name: string;
  address: string;
  phoneNumber: string;
  email: string;
  openingHours: string;
  products: IProduct[];
}