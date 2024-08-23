import { Component } from '@angular/core';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { IDelivery } from '@interfaces/delivery.interface';

@Component({
  selector: 'app-delivery',
  standalone: true,
  imports: [],
  templateUrl: './delivery.component.html',
})
export class DeliveryComponent {
  deliveryOptions: IDelivery[] = [];
  selectedOption: number | null = null;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadDeliveryOptions();
  }

  loadDeliveryOptions(): void {
    this.userService.getDeliveryOptions().subscribe({
      next: (options) => {
        this.deliveryOptions = options;
      },
      error: (err) => {
        console.error('Failed to load delivery options', err);
      }
    });
  }

  confirmDelivery(): void {
    if (this.selectedOption !== null) {
      this.userService.confirmDelivery(this.selectedOption).subscribe({
        next: () => {
          console.log('Delivery option confirmed');
        },
        error: (err) => {
          console.error('Failed to confirm delivery', err);
        }
      });
    }
  }
}