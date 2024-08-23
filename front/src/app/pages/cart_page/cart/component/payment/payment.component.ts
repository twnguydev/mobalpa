import { Component } from '@angular/core';
import { UserService } from '@services/user.service';
import { IPayment } from '@interfaces/payment.interface';

@Component({
  selector: 'app-payment',
  standalone: true,
  imports: [],
  templateUrl: './payment.component.html',
})
export class PaymentComponent {
  paymentMethods: IPayment[] = [];
  selectedMethod: number | null = null;

  constructor(private userService: UserService) { }

  ngOnInit(): void {
    this.loadPaymentMethods();
  }

  loadPaymentMethods(): void {
    this.userService.getPaymentMethods().subscribe({
      next: (methods) => {
        this.paymentMethods = methods;
      },
      error: (err) => {
        console.error('Failed to load payment methods', err);
      }
    });
  }

  confirmPayment(): void {
    if (this.selectedMethod !== null) {
      this.userService.confirmPayment(this.selectedMethod).subscribe({
        next: () => {
          console.log('Payment confirmed');
        },
        error: (err) => {
          console.error('Failed to confirm payment', err);
        }
      });
    }
  }
}