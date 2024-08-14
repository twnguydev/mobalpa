import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent {
  wishlist: IWishlist | null = null;
  wishlistNotFound: boolean = false;

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
        this.wishlistNotFound = false;
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