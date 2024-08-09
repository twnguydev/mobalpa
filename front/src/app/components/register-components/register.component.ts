import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from './../../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { NewsletterService } from './../../../services/newsletter.service';
import { HttpErrorResponse } from '@angular/common/http';

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
  formSubmitted: boolean = false;
  errorNewsletter: string = '';

  constructor(
    private formBuilder: FormBuilder,
    private authService: AuthService,
    private newsletterService: NewsletterService,
    private router: Router
  ) {
    this.form = this.formBuilder.group({
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      phoneNumber: ['', [Validators.required, Validators.pattern("[0-9]{10}")]],
      birthdate: ['', Validators.required],
      password: ['', [Validators.required, Validators.minLength(6)]],
      confirmPassword: ['', [Validators.required, Validators.minLength(6)]],
      terms: [false, Validators.requiredTrue],
      newsletter: [false]
    }, { validators: this.passwordValidator });
  }

  ngOnInit() {}

  passwordValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const password = control.get('password');
    const confirmPassword = control.get('confirmPassword');
    if (!password || !confirmPassword) {
      return null;
    }
    return password.value === confirmPassword.value ? null : { passwordNotMatching: true };
  }

  onSubmit() {
    this.formSubmitted = true;
    if (this.form.valid) {
      console.log('Form data:', this.form.value);

      const formData = {
        ...this.form.value,
        address: null,
        city: null,
        zipCode: null,
      };

      this.authService.signup(formData).subscribe({
        next: (response: string) => {
          this.errorMessage = '';
          this.successMessage = 'Inscription réussie ! un email de confirmation vous a été envoyé.';

          if (formData.newsletter) {
            this.addToNewsletter(formData.email);
          }
        },
        error: (error: HttpErrorResponse) => {
          console.error('Registration error', error);
          this.handleErrorResponse(error);
        }
      });
    } else {
      this.displayFormErrors();
    }
  }

  addToNewsletter(email: string) {
    this.newsletterService.addNewsletter({ emailUser: email }).subscribe({
      next: (res) => {
        console.log('Added to newsletter:', res);
      },
      error: (error) => {
        console.error('Error adding to newsletter:', error);
        this.errorNewsletter = 'Une erreur est survenue lors de l\'inscription à la newsletter.<br>Veuillez réessayer depuis votre compte.';
      }
    });
  }

  displayFormErrors() {
    Object.keys(this.form.controls).forEach(controlName => {
      const control = this.form.get(controlName);
      if (control?.invalid) {
        console.log(`The ${controlName} control has errors:`, control.errors);
      }
    });
  }

  handleErrorResponse(error: any) {
    if (typeof error === 'string') {
      this.errorMessage = error;
    } else {
      console.log('Réponse d\'erreur :', error);
      this.errorMessage = 'Une erreur est survenue pendant l\'inscription. Veuillez réessayer.';
    }
    this.successMessage = '';
  }
}
