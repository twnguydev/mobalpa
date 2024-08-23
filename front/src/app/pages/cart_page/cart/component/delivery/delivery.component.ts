import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { Router } from '@angular/router';
import { IDeliveryResponse, IDeliveryMethod } from '@interfaces/order.interface';

@Component({
  selector: 'app-delivery',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './delivery.component.html',
})
export class DeliveryComponent {
  deliveryOptions: IDeliveryResponse = {};
  selectedOption: string | null = null;
  selectedDelivery: IDeliveryMethod | null = null;

  constructor(
    private userService: UserService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadDeliveryOptions();
  }

  loadDeliveryOptions(): void {
    this.orderService.getDeliveryOptions().subscribe({
      next: (options: IDeliveryResponse) => {
        this.deliveryOptions = options;
        console.log('Delivery options loaded', this.deliveryOptions);
      },
      error: (err) => {
        console.error('Failed to load delivery options', err);
      }
    });
  }

  confirmDelivery(): void {
    if (this.selectedOption) {
      this.selectedDelivery = this.deliveryOptions[this.selectedOption] || null;

      if (this.selectedDelivery) {
        localStorage.setItem('selectedDelivery', JSON.stringify({
          name: this.selectedOption,
          ...this.selectedDelivery
        }));

        console.log('Delivery confirmed', this.selectedDelivery);
        this.router.navigate(['/commande/paiement']);
      } else {
        console.error('Selected delivery option not found');
      }
    } else {
      console.error('No delivery option selected');
    }
  }

  getDeliveryKeys(): string[] {
    return Object.keys(this.deliveryOptions);
  }
}