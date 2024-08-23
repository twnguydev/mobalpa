import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';
import { OrderService } from '@services/order.service';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';
import { ICouponCodeResponse } from '@interfaces/order.interface';
import { IOrder } from '@interfaces/order.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule
  ],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {
  cart: IWishlistItem[] = [];
  productAdded: { [key: string]: boolean } = {};
  couponCode: string = '';
  couponCodeMessage: { success: string, error: string } = { success: '', error: '' };
  couponAmount: number = 0;
  couponType: "PERCENTAGE" | "AMOUNT" = "PERCENTAGE";

  constructor(
    private userService: UserService,
    private authService: AuthService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.cart = this.userService.getCartFromLocalstorage();
    // console.log('Cart loaded', this.cart);

    this.cart.forEach(item => {
      item.product.oldPrice = item.product?.price;
      const maxDiscountRate = this.getMaxDiscountRate(item);
      item.product.discountRate = maxDiscountRate;
      item.product.newPrice = this.getDiscountedPrice(item.product?.price, maxDiscountRate);
    });
  }

  getMaxDiscountRate(item: IWishlistItem): number {
    return item.product?.campaigns.reduce((maxRate: number, campaign: any) => {
      return campaign.discountRate > maxRate ? campaign.discountRate : maxRate;
    }, 0);
  }

  getDiscountedPrice(price: number | null, discountRate: number): number {
    return price ? price - (price * discountRate / 100) : 0;
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
    return this.cart.reduce((acc, item) => {
      const price = item.product?.newPrice ?? item.product?.price ?? 0;
      return acc + (price * item.quantity);
    }, 0);
  }

  calculateShippingSavings(): number {
    const shippingCost = 20;
    return this.calculateSubtotal() >= 100 ? shippingCost : 0;
  }

  calculateSavings(): number {
    let savings = 0;
    
    if (this.couponType === 'AMOUNT') {
      savings += this.couponAmount;
    } else if (this.couponType === 'PERCENTAGE') {
      savings += this.calculateSubtotal() * (this.couponAmount / 100);
    }
    
    savings += this.calculateShippingSavings();
    
    return savings;
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

  checkPromoCode(code: string): void {
    if (!code) {
      this.couponCodeMessage.error = 'Veuillez entrer un code promotionnel';
      this.couponCodeMessage.success = '';
      return;
    }

    this.orderService.testPromoCode(code).subscribe({
      next: (response: ICouponCodeResponse) => {
        console.log('Promo code applied', response);
        this.couponAmount = response.discountRate;
        this.couponType = response.discountType;
        this.couponCodeMessage.success = 'Code promotionnel appliqué avec succès';
        this.couponCodeMessage.error = '';
      },
      error: (error) => {
        console.error('Failed to apply promo code', error);
        this.couponCodeMessage.error = 'Code promotionnel invalide ou déjà utilisé';
        this.couponCodeMessage.success = '';
      }
    });
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

  confirmOrder(): void {


    const order: IOrder = {
      uuid: '',
      userId: this.authService.user?.uuid ?? '',
      items: this.cart.map(item => ({
        productUuid: item.productUuid,
        quantity: item.quantity,
        product: item.product,
      })),
      vat: this.calculateVAT(),
      reduction: this.calculateSavings(),
      totalHt: this.calculateSubtotal(),
      totalTtc: this.calculateTotalCart(),
      status: 'PENDING',
      deliveryFees: this.estimateShipping(),
      deliveryAddress: '',
    };

    this.orderService.saveTempOrder(order);
    this.router.navigate(['/commande/confirmation']);
  }
}
