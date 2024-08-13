import { IProduct } from '@interfaces/product.interface';
import { IImage } from '@interfaces/image.interface';

export interface ICategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  image: IImage;
  subcategories: ISubcategory[];
  isOpen?: boolean;
}

export interface ISubcategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  category: ICategory;
  products: IProduct[];
}