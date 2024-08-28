import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@services/auth.service';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { Router } from '@angular/router';
import { IDeliveryResponse, IDeliveryMethod } from '@interfaces/order.interface';
import { IOrder, IOrderItem } from '@interfaces/order.interface';
import { IPayment } from '@interfaces/payment.interface';
import { IUser, IVisitor } from '@interfaces/user.interface';

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
  completedOrder: IOrder | null = null;
  user: IUser | null = null;

  successMessage: { payment: string, address: string, order: string } | null = null;
  errorMessage: { payment: string, address: string, order: string } | null = null;

  newPayment: IPayment = {} as IPayment;
  isPaymentFormVisible: boolean = false;

  newAddress: { line1: string; city: string; postalCode: string } = { line1: '', city: '', postalCode: '' };
  isAddressFormVisible: boolean = false;

  isUserLoggedIn: boolean = false;
  userAddress: { line1: string; city: string; postalCode: string } | null = null;
  userPaymentMethods: IPayment[] = [];
  selectedPaymentMethod: IPayment | null = null;
  tempAddress = { line1: '', city: '', postalCode: '' };
  tempPaymentMethod: IPayment = {} as IPayment;
  tempVisitor: IVisitor = {} as IVisitor;

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
        for (const key in options) {
          if (options.hasOwnProperty(key)) {
            options[key].name = key;
          }
        }

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

  processOrder(): void {
    if (!this.authService.user) {
        if (!this.order ||
            !this.tempVisitor.email ||
            !this.tempVisitor.firstname ||
            !this.tempVisitor.lastname ||
            !this.tempVisitor.phoneNumber ||
            !this.tempAddress.line1 ||
            !this.tempAddress.city ||
            !this.tempAddress.postalCode ||
            !this.selectedOption ||
            !this.deliveryOptions[this.selectedOption] ||
            !this.tempPaymentMethod) {
            this.errorMessage = {
                payment: '',
                address: '',
                order: 'Veuillez renseigner toutes les informations nécessaires avant de passer la commande.'
            };
            console.log('Informations manquantes pour un visiteur.');
            console.table({
                visitor: this.tempVisitor,
                address: this.tempAddress,
                selectedOption: this.selectedOption,
                deliveryOptions: this.deliveryOptions,
                selectedPaymentMethod: this.selectedPaymentMethod
            });
            return;
        }

        const visitor: IVisitor = {
            email: this.tempVisitor.email,
            firstname: this.tempVisitor.firstname,
            lastname: this.tempVisitor.lastname,
            phoneNumber: this.tempVisitor.phoneNumber,
            address: this.tempAddress.line1,
            city: this.tempAddress.city,
            zipcode: this.tempAddress.postalCode
        };

        this.authService.signupVisitor(visitor).subscribe(
            (user) => {
                this.tempVisitor = user;
                console.log('Visiteur enregistré :', user);

                if (this.tempPaymentMethod && this.tempPaymentMethod.expirationDate) {
                  const [year, month] = this.tempPaymentMethod.expirationDate.split('-');
                  const formattedDate = `${year}-${month}-01T00:00:00`;
                  this.tempPaymentMethod.expirationDate = formattedDate;
                }                

                const visitorPayment: IPayment = {
                    ...this.tempPaymentMethod,
                    userUuid: this.tempVisitor.uuid || '',
                    cardHolder: `${this.tempVisitor.firstname} ${this.tempVisitor.lastname}`,
                    paymentMethod: 'CREDIT_CARD'
                };

                this.userService.addPaymentVisitor(this.tempVisitor.uuid || '', visitorPayment).subscribe(
                    (payment) => {
                        console.log('Méthode de paiement ajoutée pour le visiteur :', payment);

                        if (!this.order || !this.selectedOption) return;
                        const deliveryMethod = this.deliveryOptions[this.selectedOption];

                        this.createOrder(
                            deliveryMethod,
                            `${visitor.address}, ${visitor.zipcode} ${visitor.city}`,
                            payment.uuid || '',
                            user.uuid || '',
                            this.order.reduction,
                            this.order.totalHt,
                            this.order.items
                        );
                    },
                    (error) => {
                        console.error('Échec de l\'ajout de la méthode de paiement pour le visiteur :', error);
                        this.errorMessage = {
                            payment: 'Erreur lors de l\'ajout de la méthode de paiement.',
                            address: '',
                            order: ''
                        };
                    }
                );
            },
            (error) => {
                console.error('Erreur lors de l\'inscription du visiteur :', error);
                this.errorMessage = {
                    payment: '',
                    address: '',
                    order: 'Erreur lors de l\'inscription du visiteur.'
                };
            }
        );
    } else {
        if (!this.order ||
            !this.selectedOption ||
            !this.deliveryOptions[this.selectedOption] ||
            !this.selectedPaymentMethod) {
            this.errorMessage = {
                payment: '',
                address: '',
                order: 'Veuillez renseigner toutes les informations nécessaires avant de passer la commande.'
            };
            return;
        }

        this.createOrder(
            this.deliveryOptions[this.selectedOption],
            `${this.userAddress?.line1}, ${this.userAddress?.postalCode} ${this.userAddress?.city}`,
            this.selectedPaymentMethod.uuid,
            this.authService.user.uuid,
            this.order.reduction,
            this.order.totalHt,
            this.order.items
        );
    }
}

  createOrder(
    deliveryMethod: IDeliveryMethod,
    deliveryAddress: string,
    paymentUuid: string,
    userUuid: string,
    reduction: number,
    totalHt: number,
    items: IOrderItem[]
  ): void {
    const orderDetails: IOrder = {
      deliveryMethod: deliveryMethod.name,
      deliveryAddress,
      paymentUuid,
      userUuid,
      reduction,
      totalHt,
      items,
    };

    console.log('Creating order:', orderDetails);

    this.orderService.createOrder(orderDetails).subscribe(
      (order) => {
        this.orderService.saveTempOrder(order);
        this.completeOrder(order.orderUuid || '');
      },
      (error) => {
        console.error('Failed to create order:', error);
        this.errorMessage = {
          payment: '',
          address: '',
          order: 'Erreur lors de la création de la commande.'
        };
      }
    );
  }

  completeOrder(orderUuid: string): void {
    this.orderService.completeOrder(orderUuid).subscribe(
      (order) => {
        console.log('Order completed:', order);
        this.orderService.saveTempOrder(order);
        this.router.navigate(['/commande/details']);
      },
      (error) => {
        console.error('Failed to complete order:', error);
        this.errorMessage = {
          payment: '',
          address: '',
          order: 'Erreur lors de la finalisation de la commande.'
        };
      }
    )
  }

  addPayment(): void {
    const uuid = this.authService.user?.uuid;
    if (uuid) {
      if (this.newPayment.paymentMethod === 'CREDIT_CARD') {
        if (!this.newPayment.cardHolder || !this.newPayment.cardNumber || !this.newPayment.expirationDate || !this.newPayment.cvv) {
          this.errorMessage = { payment: 'Veuillez remplir tous les champs de la carte de crédit.', address: '', order: '' };
          return;
        }

        const [year, month] = this.newPayment.expirationDate.split('-');
        const formattedDate = `${year}-${month}-01T00:00:00`;
        this.newPayment.expirationDate = formattedDate;

      } else if (this.newPayment.paymentMethod === 'PAYPAL') {
        if (!this.newPayment.paypalEmail) {
          this.errorMessage = { payment: 'Veuillez renseigner l\'adresse email PayPal.', address: '', order: '' };
          return;
        }
      }

      this.newPayment.userUuid = uuid;
      this.userService.addPayment(uuid, this.newPayment).subscribe(
        payment => {
          this.userPaymentMethods.push(payment);
          this.newPayment = {} as IPayment;
          this.successMessage = { payment: 'Méthode de paiement ajoutée avec succès!', address: '', order: '' };
          this.isPaymentFormVisible = false;
        },
        error => {
          console.error('Erreur lors de l\'ajout de la méthode de paiement:', error);
          this.errorMessage = { payment: 'Erreur lors de l\'ajout de la méthode de paiement.', address: '', order: '' };
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
      this.errorMessage = { address: 'Veuillez remplir tous les champs de l\'adresse.', payment: '', order: '' };
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
        this.successMessage = { address: 'Modifications enregistrées avec succès!', payment: '', order: '' };
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
        this.errorMessage = { address: 'Erreur lors de l\'enregistrement des modifications.', payment: '', order: '' };
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