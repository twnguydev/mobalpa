import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { IOrder } from '@interfaces/order.interface';

@Component({
  selector: 'app-order-details',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './order-details.component.html',
})
export class OrderDetailsComponent {
  [x: string]: any;
  order: any | null = null;
  deliveryNumbersString: string = '';

  constructor(private orderService: OrderService) { }

  ngOnInit(): void {
    this.order = this.orderService.getTempOrder();
    this.deliveryNumbersString = this.order.deliveryNumbers.join(', ');

    console.log(this.order);

    setTimeout(() => {
      this.orderService.clearTempOrder();
    }, 15000);
  }
}