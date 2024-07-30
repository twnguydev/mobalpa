export interface IWishlist {
  id: string;
  items: IWishlistItem[];
}

export interface IWishlistItem {
  id: string;
  name: string;
  price: number;
}