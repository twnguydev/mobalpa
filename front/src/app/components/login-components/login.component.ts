import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
  imports: [ReactiveFormsModule, CommonModule, FormsModule]
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  forgotPasswordForm: FormGroup;
  submitted = false;
  showPassword = false;
  forgotPasswordVisible = false;
  errorMessage: string = '';
  rememberMe: boolean = false;

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
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
      this.rememberMe = true;
    }

    if (storedPassword) {
      this.form.get('password')?.setValue(this.dehashPassword(storedPassword));
    }
  }

  async onSubmit() {
    this.submitted = true;
    if (this.form.valid) {
      const { email, password } = this.form.value;

      this.authService.login(email, password).subscribe({
        next: response => {
          console.log('Login successful', response);
          if (this.rememberMe) {
            const hashedPassword = this.hashPassword(password);
            localStorage.setItem('rememberedEmail', email);
            localStorage.setItem('rememberedPassword', hashedPassword);
          } else {
            localStorage.removeItem('rememberedEmail');
            localStorage.removeItem('rememberedPassword');
          }
          // this.router.navigate(['/Profile']);
        },
        error: error => {
          console.error('Login error', error);
          this.errorMessage = 'Invalid email or password. Please try again.';
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
          if (error && error.error && typeof error.error === 'string') {
            this.errorMessage = error.error;
          } else {
            this.errorMessage = 'Erreur lors de l\'envoi de l\'email de réinitialisation. Veuillez réessayer.';
          }
        }
      });
    }
  }
}
