import { IProduct } from '@interfaces/product.interface';
import { IImage } from '@interfaces/image.interface';
import { ICampaign } from '@interfaces/product.interface';

export interface ICategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  images: IImage[];
  subcategories: ISubcategory[];
  isOpen?: boolean;
  campaigns: ICampaign[];
}

export interface ISubcategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  category: ICategory;
  products: IProduct[];
  campaigns: ICampaign[];
}