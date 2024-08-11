import { IProduct } from '@interfaces/product.interface';

export interface ICategory {
  uuid: string;
  name: string;
  description: string;
  subcategories: ISubcategory[];
}

export interface ISubcategory {
  uuid: string;
  name: string;
  description: string;
  category: ICategory;
  products: IProduct[];
}