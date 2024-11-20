import { ICategory, ISubcategory } from '@interfaces/category.interface';
// import { ICategory } from '@interfaces/category.interface';
// import { ISubcategory } from '@interfaces/subcategory.interface'

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
  campaigns: ICampaign[];
  oldPrice?: number;
  newPrice?: number;
  discountRate?: number;
  createdAt?: Date;

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

export interface ICampaign {
  id: string;
  name: string;
  discountRate: number;
  dateStart: string;
  dateEnd: string;
  targetUuid: string;
  type: 'PRODUCT' | 'SUBCATEGORY' | 'CATEGORY';
}
