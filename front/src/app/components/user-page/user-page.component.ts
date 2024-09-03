import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '@services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser } from '@interfaces/user.interface';
import { IWishlist } from '@interfaces/wishlist.interface';
import { ITicket } from '@interfaces/tickets.interface';
import { ICoupon } from '@interfaces/coupon.interface';
import { IPayment } from '@interfaces/payment.interface';

@Component({
  selector: 'app-user-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {
  user: IUser | null = null;
  wishlists: IWishlist[] = [];
  tickets: ITicket[] = [];
  coupons: ICoupon[] = [];
  payments: IPayment[] = [];
  isEditing: boolean = false;
  editedUser: Partial<IUser> = {};
  successMessage: string = '';
  errorMessage: string = '';

  availableRoles = [
    { id: 1, name: 'ROLE_USER' },
    { id: 2, name: 'ROLE_ADMIN' },
    { id: 3, name: 'ROLE_STORE_MANAGER' }
  ];

  constructor(
    private adminService: AdminService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.loadUserData(uuid);
    }
  }

  loadUserData(uuid: string): void {
    this.adminService.getUserByUuid(uuid).subscribe({
      next: (data) => {
        this.user = data;
        this.editedUser = { ...data };
        this.tickets = data.tickets ?? [];
      },
      error: (err) => console.error('Erreur lors du chargement des détails utilisateur', err)
    });

    this.adminService.getUserCoupons(uuid).subscribe({
      next: (couponsData) => {
        this.coupons = couponsData;
      },
      error: (err) => console.error('Erreur lors du chargement des coupons utilisateur', err)
    });

    this.adminService.getPaymentsByUserUuid(uuid).subscribe({
      next: (paymentsData) => {
        this.payments = paymentsData;
      },
      error: (err) => console.error('Erreur lors du chargement des paiements utilisateur', err)
    });
  }

  toggleEdit(): void {
    this.isEditing = !this.isEditing;
    if (!this.isEditing && this.user) {
      this.editedUser = { ...this.user };
    }
  }

  saveChanges(): void {
    if (this.user && this.user.uuid && this.editedUser) {
      const allowedFields = {
        firstname: this.editedUser.firstname,
        lastname: this.editedUser.lastname,
        email: this.editedUser.email,
        phoneNumber: this.editedUser.phoneNumber,
        birthdate: this.editedUser.birthdate,
        address: this.editedUser.address,
        zipcode: this.editedUser.zipcode,
        city: this.editedUser.city,
        roles: this.editedUser.roles?.map(role => ({ id: role.id, name: role.name }))
      };

      this.adminService.updateUser(this.user.uuid, allowedFields).subscribe({
        next: (updatedUser) => {
          this.user = updatedUser;
          this.isEditing = false;
          this.successMessage = 'Utilisateur mis à jour avec succès';
          setTimeout(() => {
            this.successMessage = '';
          }
          , 3000);
        },
        error: (err) => {
          console.error('Erreur lors de la mise à jour de l\'utilisateur', err);
          this.errorMessage = 'Erreur lors de la mise à jour de l\'utilisateur';
          setTimeout(() => {
            this.errorMessage = '';
          }, 3000);
        }
      });
    } else {
      console.error('L\'utilisateur est null ou ne contient pas d\'UUID');
      this.errorMessage = 'Une erreur du serveur est survenue';
      setTimeout(() => {
        this.errorMessage = '';
      }, 3000);
    }
  }



  goBackToAdminPanel(): void {
    this.router.navigate(['/admin']);
  }
}
