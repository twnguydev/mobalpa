import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '@services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, CommonModule]
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  forgotPasswordForm: FormGroup;
  submitted = false;
  showPassword = false;
  forgotPasswordVisible = false;
  errorMessage: string = '';
  errors: string[] = [];

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]],
      remember: [false]
    });

    this.forgotPasswordForm = this.fb.group({
      forgotPasswordEmail: ['', [Validators.required, Validators.email]]
    });
  }

  ngOnInit() {
    const storedEmail = localStorage.getItem('rememberedEmail');
    const storedPassword = localStorage.getItem('rememberedPassword');

    if (storedEmail) {
      this.form.get('email')?.setValue(storedEmail);
      this.form.get('remember')?.setValue(true);
    }

    if (storedPassword) {
      this.form.get('password')?.setValue(this.dehashPassword(storedPassword));
    }
  }

  onLoginSubmit() {
    this.submitted = true;
    this.errors = [];

    if (this.form.valid) {
      const { email, password, remember } = this.form.value;

      this.authService.login(email, password).subscribe({
        next: response => {
          console.log('Login successful', response);

          // Check the role of the user
          const roles = response.user.roles.map((role: any) => role.name);

          if (remember) {
            const hashedPassword = this.hashPassword(password);
            localStorage.setItem('rememberedEmail', email);
            localStorage.setItem('rememberedPassword', hashedPassword);
          } else {
            localStorage.removeItem('rememberedEmail');
            localStorage.removeItem('rememberedPassword');
          }

          if (roles.includes('ROLE_ADMIN')) {
            this.router.navigate(['/admin']);
          } else {
            this.router.navigate(['/profil']);
          }
        },
        error: error => {
          console.error('Login error', error);
          this.errors = error.errors || ['Une erreur est survenue. Veuillez réessayer.'];
        }
      });
    }
  }

  private hashPassword(password: string): string {
    return btoa(password);
  }

  private dehashPassword(hashedPassword: string): string {
    return atob(hashedPassword);
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
    const passwordField: HTMLInputElement = document.getElementById('password') as HTMLInputElement;
    passwordField.type = this.showPassword ? 'text' : 'password';
  }

  showForgotPassword() {
    this.forgotPasswordVisible = true;
  }

  hideForgotPassword() {
    this.forgotPasswordVisible = false;
  }

  onForgotPassword() {
    if (this.forgotPasswordForm.valid) {
      const email = this.forgotPasswordForm.get('forgotPasswordEmail')?.value;
      this.authService.forgotPassword(email).subscribe({
        next: response => {
          console.log('Password reset email sent', response);
          this.forgotPasswordVisible = false;
          alert('Un email de réinitialisation a été envoyé à votre adresse.');
        },
        error: error => {
          console.error('Forgot password error', error);
          this.handleForgotPasswordError(error);
        }
      });
    }
  }

  private handleForgotPasswordError(error: any) {
    if (error && error.error && typeof error.error === 'string') {
      this.errorMessage = error.error;
    } else {
      this.errorMessage = 'Erreur lors de l\'envoi de l\'email de réinitialisation. Veuillez réessayer.';
    }
  }
}
