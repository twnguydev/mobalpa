import { Component, OnInit, OnDestroy } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { IUser } from '@interfaces/user.interface';
import { AuthService } from '@services/auth.service';
import { UserService } from '@services/user.service';
import { OrderService } from '@services/order.service';
import { ProductService } from '@services/product.service';
import { Subscription } from 'rxjs';
import { IPayment } from '@interfaces/payment.interface';
import { IOrder } from '@interfaces/order.interface';
import { MatDialog } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '@components/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit, OnDestroy {
  tabs = [
    { title: 'Mes commandes' },
    { title: 'Adresses de livraison' },
    { title: 'Modes de paiement' },
    { title: 'Données personnelles' },
  ];

  selectedTab: number = 0;
  user: IUser = {} as IUser;
  private userSubscription: Subscription | null = null;

  successMessage: string | null = null;
  errorMessage: string | null = null;

  payments: IPayment[] = [];
  orders: IOrder[] = [];
  selectedOrder: IOrder | null = null;
  newPayment: IPayment = {} as IPayment;

  isPaymentFormVisible: boolean = false;

  constructor(
    private authService: AuthService,
    private userService: UserService,
    private orderService: OrderService,
    private productService: ProductService,
    private dialog: MatDialog
  ) { }

  ngOnInit(): void {
    if (this.authService.user) {
      this.user = this.authService.user;
      this.loadPayments(this.user.uuid);
      this.loadUserData(this.user.uuid);
      this.loadOrders(this.user.uuid);
    }

    this.userSubscription = this.authService.currentUser$.subscribe(user => {
      if (user) {
        this.user = user;
      } else {
        console.log('No user data available');
      }
    });
  }

  loadUserData(uuid: string): void {
    this.authService.loadUserData(uuid).subscribe();
  }

  loadPayments(uuid: string): void {
    this.userService.getPayments(uuid).subscribe(
      payments => {
        this.payments = payments;
      },
      error => {
        console.error('Erreur lors du chargement des méthodes de paiement:', error);
      }
    );
  }

  loadOrders(uuid: string): void {
    this.userService.getOrders(uuid).subscribe(
      orders => {
        this.orders = orders;

        for (const order of this.orders) {
          for (const item of order.items) {
            this.productService.getProductById(item.productUuid).subscribe(
              product => {
                item.product = product;
              },
              error => {
                console.error('Erreur lors du chargement des détails du produit:', error);
              }
            );
          }
        }
      },
      error => {
        console.error('Erreur lors du chargement des commandes:', error);
      }
    );
  }  

  showOrderDetails(order: IOrder): void {
    this.selectedOrder = order;
  }

  addPayment(): void {
    const uuid = this.user.uuid;
    if (uuid) {
      const [year, month] = this.newPayment.expirationDate.split('-');

      const formattedDate = `${year}-${month}-01T00:00:00`;

      this.newPayment.expirationDate = formattedDate;

      this.newPayment.userUuid = uuid;
      this.userService.addPayment(uuid, this.newPayment).subscribe(
        payment => {
          this.payments.push(payment);
          this.newPayment = {} as IPayment;
          this.successMessage = 'Méthode de paiement ajoutée avec succès!';
          this.isPaymentFormVisible = false;
        },
        error => {
          console.error('Erreur lors de l\'ajout de la méthode de paiement:', error);
          this.errorMessage = 'Erreur lors de l\'ajout de la méthode de paiement.';
        }
      );
    }
  }

  deletePayment(paymentId: string): void {
    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      data: { message: 'Êtes-vous sûr de vouloir supprimer cette méthode de paiement ?' }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        const uuid = this.user.uuid;
        if (uuid) {
          this.userService.deletePayment(uuid, paymentId).subscribe(
            () => {
              this.payments = this.payments.filter(payment => payment.uuid !== paymentId);
              this.successMessage = 'Méthode de paiement supprimée avec succès!';
            },
            error => {
              console.error('Erreur lors de la suppression de la méthode de paiement:', error);
              this.errorMessage = 'Erreur lors de la suppression de la méthode de paiement. Veuillez réessayer.';
            }
          );
        }
      }
    });
  }

  ngOnDestroy(): void {
    this.userSubscription?.unsubscribe();
  }

  selectTab(index: number) {
    this.selectedTab = index;
    this.successMessage = null;
    this.errorMessage = null;
  }

  onSubmit() {
    const uuid = this.user.uuid;

    if (uuid) {
      this.userService.update(this.user).subscribe(
        user => {
          this.successMessage = 'Données mises à jour avec succès';
          this.authService.loadUserData(uuid).subscribe();
        },
        error => {
          console.error('Erreur lors de la mise à jour des données:', error);
          this.errorMessage = 'Erreur lors de la mise à jour des données. Veuillez réessayer.';
        }
      );
    }
  }

  togglePaymentForm(): void {
    this.isPaymentFormVisible = !this.isPaymentFormVisible;
  }
}