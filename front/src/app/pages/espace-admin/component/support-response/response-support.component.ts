import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AdminService } from '@services/admin.service';
import { AuthService } from '@services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser } from '@interfaces/user.interface';
import { ITicket } from '@interfaces/tickets.interface';

@Component({
  selector: 'app-response-support',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './response-support.component.html',
  styleUrls: ['./response-support.component.css']
})
export class ResponseSupportComponent implements OnInit {

  support: ITicket | null = null;
  user: IUser | null = null;
  showResolveForm = false;
  responderUuid: string = '';
  resolution: string = '';
  email: string = '';
  successMessage: string = '';
  errorMessage: string = '';

  constructor(
    private adminService: AdminService,
    private authService: AuthService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const ticketId = this.route.snapshot.paramMap.get('uuid');
    if (ticketId) {
      this.loadSupportData(ticketId);
    } else {
      console.error('Ticket ID is missing in the route');
    }

    this.authService.getCurrentUser().subscribe(user => {
      this.responderUuid = user?.uuid || '';
    });
  }

  loadSupportData(ticketId: string): void {
    console.log(`Loading support data for ticketId: ${ticketId}`);
    this.adminService.getTicketByUuid(ticketId).subscribe({
      next: (data) => {
        this.support = data;
        console.log('Support ticket details:', data);
        // Récupérer l'email de l'utilisateur à partir des données du ticket
        this.email = data.user?.email || '';
      },
      error: (err) => {
        console.error('Erreur lors du chargement des détails du ticket', err);
      }
    });
  }

  toggleResolveForm(): void {
    this.showResolveForm = !this.showResolveForm;
  }

  resolveTicket(): void {
    if (!this.support || !this.responderUuid || !this.resolution || !this.email) {
      this.errorMessage = 'Veuillez remplir tous les champs.';
      setTimeout(() => {
        this.errorMessage = '';
      }, 3000);
      return;
    }

    this.adminService.resolveTicket(this.support.uuid, this.responderUuid, this.resolution).subscribe(
      response => {
        this.support = response;
        this.toggleResolveForm();
        this.successMessage = 'Ticket résolu avec succès.';
        setTimeout(() => {
          this.successMessage = '';
        }, 3000);
      },
      error => {
        console.error(error);
        this.errorMessage = 'Erreur lors de la résolution du ticket.';
        setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    );
  }

  goBackToAdminPanel(): void {
    this.router.navigate(['/admin']);
  }
}
