import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@services/auth.service';

@Component({
  standalone: true,
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css'],
  imports: [CommonModule, FormsModule]
})
export class ResetPasswordComponent implements OnInit {
  token: string | null = null;
  newPassword: string = '';
  errorMessage: string | null = null;
  successMessage: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private userService: AuthService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.token = this.route.snapshot.queryParamMap.get('token');
  }

  onSubmit(): void {
    this.errorMessage = null;
    this.successMessage = null;

    if (this.token) {
      this.userService.resetPassword(this.token, this.newPassword).subscribe(
        response => {
          this.successMessage = 'Mot de passe réinitialisé avec succès';
          setTimeout(() => {
            this.router.navigate(['/login']);
          }, 2000);
        },
        error => {
          if (error.status === 400) {
            this.errorMessage = error.error || 'Erreur lors de la réinitialisation du mot de passe';
          } else {
            this.errorMessage = 'Erreur lors de la réinitialisation du mot de passe';
          }
        }
      );
    } else {
      this.errorMessage = 'Jeton de réinitialisation invalide';
    }
  }
}
