import { IProduct } from './product.interface';

export interface IWishlist {
  uuid: string;
  items: IWishlistItem[];
}

export interface IWishlistItem {
  uuid: string;
  productUuid: string;
  product: IProduct;
  quantity: number;
  selectedColor: string;
  properties: {
    brand: string;
    images: string;
  };
}
