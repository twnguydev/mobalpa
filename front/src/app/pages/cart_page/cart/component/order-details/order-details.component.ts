// import { Component } from '@angular/core';
// import { UserService } from '@services/user.service';
// import { OrderService } from '@services/order.service';
// import { IOrder } from '@interfaces/order.interface';

// @Component({
//   selector: 'app-order-details',
//   standalone: true,
//   imports: [],
//   templateUrl: './order-details.component.html',
// })
// export class OrderDetailsComponent {
//   order: IOrder | null = null;

//   constructor(private userService: UserService) { }

//   ngOnInit(): void {
//     this.loadOrderDetails();
//   }

//   loadOrderDetails(): void {
//     this.userService.getOrderDetails().subscribe({
//       next: (order) => {
//         this.order = order;
//       },
//       error: (err) => {
//         console.error('Failed to load order details', err);
//       }
//     });
//   }

//   goBack(): void {
//     // Logic to go back to the orders list
//   }
// }