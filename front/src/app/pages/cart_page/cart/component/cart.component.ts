import { Component } from '@angular/core';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';
import { IProduct } from '@interfaces/product.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  cart: IWishlistItem[] = [];
  productAdded: { [key: string]: boolean } = {};

  constructor(
    private userService: UserService,
    private authService: AuthService
  ) { }

  ngOnInit(): void {
    this.cart = this.userService.getCartFromLocalstorage()
  }

  decreaseQuantity(item: IWishlistItem): void {
    if (item.quantity > 1) {
      item.quantity--;
      this.userService.modifyCartFromLocalstorage('remove', item);
    }
  }

  increaseQuantity(item: IWishlistItem): void {
    item.quantity++;
    this.userService.modifyCartFromLocalstorage('add', item);
  }

  calculateSubtotal(): number {
    return this.cart.reduce((acc, item) => acc + (item.product ? item.product.price : 0) * item.quantity, 0);
  }

  calculateSavings(): number {
    return this.calculateSubtotal() * 0.10;
  }

  estimateShipping(): number {
    return this.calculateSubtotal() >= 100 || this.calculateSubtotal() <= 0 ? 0 : 20;
  }

  calculateVAT(): number {
    return this.calculateSubtotal() * 0.20;
  }

  calculateTotalCart(): number {
    if (this.calculateSubtotal() < this.estimateShipping()) {
      return this.calculateSubtotal() - this.calculateSavings() + this.calculateVAT();
    } else {
      return this.calculateSubtotal() - this.calculateSavings() + this.estimateShipping() + this.calculateVAT();
    }
  }

  addToWishlist(item: IWishlistItem): void {
    if (!this.authService.isAuthenticated()) {
      this.authService.redirectToLogin();
      return;
    }
    this.userService.modifyWishlist(this.authService.user?.uuid ?? '', 'add', {
      productUuid: item.productUuid,
      selectedColor: item.selectedColor,
      quantity: item.quantity
    }).subscribe({
      next: () => {
        this.productAdded[item.productUuid] = true;

        setTimeout(() => {
          this.productAdded[item.productUuid] = false;
        }, 5000);
      },
      error: (error) => {
        console.error('Failed to add product to wishlist', error);
      }
    });
  }

  removeFromCart(item: IWishlistItem): void {
    this.userService.modifyCartFromLocalstorage('remove', item);
    this.cart = this.cart.filter(cartItem => cartItem.productUuid !== item.productUuid);
  }
}
