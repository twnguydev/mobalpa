import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule,WishlistComponent],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent {
  items!: IWishlistItem[];
  wishlist: IWishlist | null = null;
  wishlistNotFound: boolean = false;
  productAdded: { [key: string]: boolean } = {};

  constructor(private userService: UserService, private authService: AuthService) { }

  ngOnInit(): void {
    this.loadWishlist();
  }
  
  loadWishlist(): void {
    if (!this.authService.user) {
      console.error('User not logged in');
      return;
    }
  
    this.userService.getWishlist(this.authService.user.uuid).subscribe({
      next: (wishlist) => {
        console.log('Wishlist loaded', wishlist);
  
        this.wishlist = wishlist;

        if (!this.wishlist) {
          this.wishlistNotFound = true;
          return;
        }

        this.wishlist.items.forEach(item => {
          if (item.product) {
            item.product.oldPrice = item.product.price;
            const maxDiscountRate = this.getMaxDiscountRate(item);
            item.product.discountRate = maxDiscountRate;
            item.product.newPrice = this.getDiscountedPrice(item.product.price, maxDiscountRate);
          }
        });
  
        this.wishlistNotFound = false;
        console.log('Wishlist updated with prices', this.wishlist);
      },
      error: (err) => {
        if (err.status === 404) {
          console.warn('Wishlist not found');
          this.wishlistNotFound = true;
        } else {
          console.error('Failed to load wishlist', err);
        }
      }
    });
  }
  
  getMaxDiscountRate(item: IWishlistItem): number {
    if (!item.campaigns || item.campaigns.length === 0) {
      return 0;
    }
    return Math.max(...item.campaigns.map(campaign => campaign.discountRate));
  }
  
  getDiscountedPrice(price: number, discountRate: number): number {
    return price - (price * discountRate / 100);
  }  

  saveToCart(item: IWishlistItem): void {
    if (!this.authService.user) return;
    const productWithCampaign = {
      ...item.product,
      campaigns: item.campaigns || []
    };
    const cartItem = {
      productUuid: item.productUuid,
      product: productWithCampaign,
      selectedColor: item.selectedColor,
      quantity: item.quantity,
      properties: {
        brand: item.product.brand.name,
        images: item.product.images.find(image => image.color.name === item.selectedColor)?.uri || ''
      }
    };
    console.log('Adding to cart', cartItem);
    this.userService.modifyCartFromLocalstorage('add', cartItem);
    this.productAdded[item.productUuid] = true;

    setTimeout(() => {
      this.productAdded[item.productUuid] = false;
    }, 5000);
  }

  increaseQuantity(item: IWishlistItem): void {
    item.quantity = 1;
    this.updateWishlistItem('add', item);
  }

  decreaseQuantity(item: IWishlistItem): void {
    if (item.quantity > 1) {
      item.quantity = 1;
      this.updateWishlistItem('remove', item);
    } else {
      this.removeFromWishlist(item);
    }
  }

  removeFromWishlist(item: IWishlistItem): void {
    if (!this.authService.user) return;

    this.userService.modifyWishlist(this.authService.user.uuid, 'remove', { productUuid: item.productUuid }).subscribe({
      next: () => {
        this.loadWishlist();
      },
      error: (err) => {
        console.error('Failed to remove item from wishlist', err);
      }
    });
  }

  private updateWishlistItem(action: 'add' | 'remove', item: IWishlistItem): void {
    if (!this.authService.user) return;

    this.userService.modifyWishlist(this.authService.user.uuid, action, {
      productUuid: item.productUuid,
      selectedColor: item.selectedColor,
      quantity: item.quantity
    }).subscribe({
      next: () => {
        console.log('Wishlist item updated');
        this.loadWishlist();
      },
      error: (err) => {
        console.error('Failed to update wishlist item', err);
      }
    });
  }
}