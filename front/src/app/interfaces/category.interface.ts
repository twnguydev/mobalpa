import { IProduct } from '@interfaces/product.interface';
import { IImage } from '@interfaces/image.interface';
import { ICampaign } from '@interfaces/product.interface';

export interface ICategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  images: IImage[];
  subcategories: any[]; // Временно оставляем subcategories типом any
  isOpen?: boolean;
  campaigns: ICampaign[];
}

export interface ISubcategory {
  uuid: string;
  name: string;
  uri: string;
  description: string;
  category: any; // Объявляем тип ICategory как any
  products: IProduct[];
  campaigns: ICampaign[];
}

// import { IImage } from '@interfaces/image.interface';
// import { ICampaign } from '@interfaces/product.interface';
// // import { ICampaign } from '@interfaces/product.interface';

// export interface ICategory {
//   uuid: string;
//   name: string;
//   uri: string;
//   description: string;
//   images: IImage[];
//   subcategories: any[]; // Временно оставляем subcategories типом any
//   isOpen?: boolean;
//   campaigns: ICampaign[];
// }
