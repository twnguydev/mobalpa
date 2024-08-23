import { IProduct, ICampaign } from './product.interface';

export interface IWishlist {
  uuid: string;
  items: IWishlistItem[];
}

export interface IWishlistItem {
  uuid?: string | null;
  productUuid: string;
  product: IProduct;
  quantity: number;
  selectedColor: string;
  campaigns?: ICampaign[];
  properties: {
    brand: string;
    images: string;
  };
}