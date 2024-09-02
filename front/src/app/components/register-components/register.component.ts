import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl, ValidatorFn } from '@angular/forms';
import { AuthService } from './../../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NewsletterService } from '@services/newsletter.service';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {
  form: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  errors: string[] = [];
  formSubmitted: boolean = false;
  showPassword = false;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private newsletterService: NewsletterService
  ) {
    this.form = this.fb.group({
      lastname: ['', [Validators.required, Validators.minLength(3)]],
      firstname: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern("[0-9]{10}")]],
      birthdate: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
      terms: [false, Validators.requiredTrue],
      communications: [false]
    }, { validators: this.passwordMatchValidator });
  }

  ngOnInit() {}

  passwordMatchValidator: ValidatorFn = (group: AbstractControl): { [key: string]: boolean } | null => {
    const password = group.get('password')?.value;
    const confirmPassword = group.get('confirmPassword')?.value;
    return password === confirmPassword ? null : { passwordMismatch: true };
  }

  onSubmit() {
    if (this.form.valid) {
      this.authService.signup(this.form.value).subscribe({
        next: (response) => {
          if (response.errors) {
            this.errors = response.errors;
          } else {
            this.errors = [];
            this.successMessage = 'Inscription réussie ! Vous allez être redirigé vers la page de connexion';

            if (this.form.get('communications')?.value && response.userUuid) { 
              this.newsletterService.addNewsletter(response.userUuid).subscribe({
                next: (newsletterResponse) => {
                  console.log('Abonnement à la newsletter réussi', newsletterResponse);
                },
                error: (err) => {
                  console.error('Erreur lors de l\'abonnement à la newsletter', err);
                }
              });
            }

            setTimeout(() => {
              this.router.navigate(['/auth/connexion']);
            }, 5000);
          }
        },
        error: (err) => {
          console.error(err);
          this.errors = ['Erreur lors de l\'inscription'];
        }
      });
    } else {
      this.logFormErrors();
    }
  }


  logFormErrors() {
    Object.keys(this.form.controls).forEach(controlName => {
      const control = this.form.get(controlName);
      if (control?.invalid) {
        console.log(`Control ${controlName} has errors:`, control.errors);
      }
    });
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
    const passwordField: HTMLInputElement = document.getElementById('password') as HTMLInputElement;
    passwordField.type = this.showPassword ? 'text' : 'password';
  }

  toggleConfirmPasswordVisibility() {
    this.showPassword = !this.showPassword;
    const passwordField: HTMLInputElement = document.getElementById('confirmPassword') as HTMLInputElement;
    passwordField.type = this.showPassword ? 'text' : 'confirmPassword';
  }
}
