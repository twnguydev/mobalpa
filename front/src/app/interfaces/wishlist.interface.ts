import { IProduct } from './product.interface';

export interface IWishlist {
  uuid: string;
  items: IWishlistItem[];
}

export interface IWishlistItem {
  uuid?: string | null;
  productUuid: string;
  product?: IProduct | null;
  quantity: number;
  selectedColor: string;
  properties: {
    brand: string;
    images: string;
  };
}