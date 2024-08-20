import { Component } from '@angular/core';
import { UserService } from '@services/user.service';
import { IWishlistItem } from '@interfaces/wishlist.interface';
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

  constructor(
    private userService: UserService
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

  calculateTotalCart(): number {
    return this.cart.reduce((acc, item) => acc + (item.product ? item.product.price : 0) * item.quantity, 0);
  }
}
