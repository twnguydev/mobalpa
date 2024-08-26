import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@services/auth.service';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { Router } from '@angular/router';
import { IDeliveryResponse, IDeliveryMethod } from '@interfaces/order.interface';
import { IOrder } from '@interfaces/order.interface';
import { IPayment } from '@interfaces/payment.interface';
import { IUser } from '@interfaces/user.interface';

@Component({
  selector: 'app-confirm-order',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './confirm-order.component.html',
})
export class ConfirmOrderComponent implements OnInit {
  deliveryOptions: IDeliveryResponse = {};
  selectedOption: string | null = null;
  selectedDelivery: IDeliveryMethod | null = null;
  order: IOrder | null = null;
  user: IUser | null = null;

  successMessage: { payment: string, address: string } | null = null;
  errorMessage: { payment: string, address: string } | null = null;

  newPayment: IPayment = {} as IPayment;
  isPaymentFormVisible: boolean = false;

  newAddress: { line1: string; city: string; postalCode: string } = { line1: '', city: '', postalCode: '' };
  isAddressFormVisible: boolean = false;

  isUserLoggedIn: boolean = false;
  userAddress: { line1: string; city: string; postalCode: string } | null = null;
  userPaymentMethods: IPayment[] = [];
  selectedPaymentMethod: IPayment | null = null;
  tempAddress = { line1: '', city: '', postalCode: '' };
  tempPaymentMethod: IPayment = { uuid: '', userUuid: '', cardHolder: '', paymentMethod: 'CREDIT_CARD', cardNumber: '', expirationDate: '', cvv: '' };

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private orderService: OrderService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadUserDetails();
    this.loadDeliveryOptions();
    this.loadOrderDetails();
  }

  loadUserDetails(): void {
    const user = this.authService.user;
    if (user) {
      this.isUserLoggedIn = true;
      this.userAddress = {
        line1: user.address || '',
        city: user.city || '',
        postalCode: user.zipcode || '',
      };
      this.userService.getPayments(user.uuid).subscribe({
        next: (payments) => {
          this.userPaymentMethods = payments;
        },
        error: (err) => {
          console.error('Failed to load user payment methods', err);
        },
      });
      this.user = user;

      console.log('User details loaded', user);
      console.log('User address', this.userAddress);
      console.log('User payment methods', this.userPaymentMethods);
    }
  }

  loadDeliveryOptions(): void {
    this.orderService.getDeliveryOptions().subscribe({
      next: (options: IDeliveryResponse) => {
        this.deliveryOptions = options;
      },
      error: (err) => {
        console.error('Failed to load delivery options', err);
      },
    });
  }

  loadOrderDetails(): void {
    this.order = this.orderService.getTempOrder();
    console.log('Order loaded', this.order);
  }

  processToPayment(): void {
    if (this.selectedOption) {
      this.selectedDelivery = this.deliveryOptions[this.selectedOption] || null;

      if (this.selectedDelivery) {
        localStorage.setItem('selectedDelivery', JSON.stringify({
          name: this.selectedOption,
          ...this.selectedDelivery,
        }));

        if (!this.isUserLoggedIn && (!this.tempAddress.line1 || !this.tempAddress.city || !this.tempAddress.postalCode)) {
          console.error('Adresse invalide pour le visiteur');
          return;
        }

        this.router.navigate(['/commande/paiement']);
      } else {
        console.error('Selected delivery option not found');
      }
    } else {
      console.error('No delivery option selected');
    }
  }

  addPayment(): void {
    const uuid = this.authService.user?.uuid;
    if (uuid) {
      if (this.newPayment.paymentMethod === 'CREDIT_CARD') {
        if (!this.newPayment.cardHolder || !this.newPayment.cardNumber || !this.newPayment.expirationDate || !this.newPayment.cvv) {
          this.errorMessage = { payment: 'Veuillez remplir tous les champs de la carte de crédit.', address: '' };
          return;
        }

        const [year, month] = this.newPayment.expirationDate.split('-');
        const formattedDate = `${year}-${month}-01T00:00:00`;
        this.newPayment.expirationDate = formattedDate;

      } else if (this.newPayment.paymentMethod === 'PAYPAL') {
        if (!this.newPayment.paypalEmail) {
          this.errorMessage = { payment: 'Veuillez renseigner l\'adresse email PayPal.', address: '' };
          return;
        }
      }

      this.newPayment.userUuid = uuid;
      this.userService.addPayment(uuid, this.newPayment).subscribe(
        payment => {
          this.userPaymentMethods.push(payment);
          this.newPayment = {} as IPayment;
          this.successMessage = { payment: 'Méthode de paiement ajoutée avec succès!', address: '' };
          this.isPaymentFormVisible = false;
        },
        error => {
          console.error('Erreur lors de l\'ajout de la méthode de paiement:', error);
          this.errorMessage = { payment: 'Erreur lors de l\'ajout de la méthode de paiement.', address: '' };
        }
      );
    }
  }

  toggleAddressForm(): void {
    this.isAddressFormVisible = !this.isAddressFormVisible;
    this.successMessage = null;
    this.errorMessage = null;
  }

  addAddress(): void {
    const user = this.authService.user;
    const uuid = user?.uuid;

    if (!uuid) return;

    if (!this.newAddress.line1 || !this.newAddress.city || !this.newAddress.postalCode) {
      this.errorMessage = { address: 'Veuillez remplir tous les champs de l\'adresse.', payment: '' };
      return;
    }

    const userUpdates: Partial<IUser> = {
      firstname: user.firstname,
      lastname: user.lastname,
      email: user.email,
      phoneNumber: user.phoneNumber,
      birthdate: user.birthdate,
      address: this.newAddress.line1,
      city: this.newAddress.city,
      zipcode: this.newAddress.postalCode
    };

    this.userService.update(uuid, userUpdates).subscribe(
      (updatedUser) => {
        this.successMessage = { address: 'Modifications enregistrées avec succès!', payment: '' };
        this.errorMessage = null;
        this.userAddress = {
          line1: this.newAddress.line1,
          city: this.newAddress.city,
          postalCode: this.newAddress.postalCode
        };
        this.newAddress = { line1: '', city: '', postalCode: '' };
        this.isAddressFormVisible = false;
        this.authService.setUser(updatedUser);
      },
      (error) => {
        this.errorMessage = { address: 'Erreur lors de l\'enregistrement des modifications.', payment: '' };
        this.successMessage = null;
      }
    );
  }

  choosePayment(paymentId: string): void {
    this.selectedPaymentMethod = this.userPaymentMethods.find(payment => payment.uuid === paymentId) || null;
  }

  getDeliveryKeys(): string[] {
    return Object.keys(this.deliveryOptions);
  }

  togglePaymentForm(): void {
    this.isPaymentFormVisible = !this.isPaymentFormVisible;
    this.newPayment = {} as IPayment;
    this.successMessage = null;
    this.errorMessage = null;
  }

  resetPaymentForm(): void {
    this.newPayment = {
      paymentMethod: this.newPayment.paymentMethod,
      cardHolder: '',
      cardNumber: '',
      expirationDate: '',
      cvv: '',
      paypalEmail: ''
    } as IPayment;
    this.successMessage = null;
    this.errorMessage = null;
  }

  changePaymentMethod(): void {
    this.resetPaymentForm();
  }


}