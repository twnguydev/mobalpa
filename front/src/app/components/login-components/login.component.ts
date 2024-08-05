import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from './../../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  form: FormGroup;
  successMessage: string = '';
  errorMessage: string = '';
  formSubmitted: boolean = false; 
  showPassword = false;


  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      email: ['', [Validators.required, Validators.email]],
      password: ['', [Validators.required, Validators.minLength(6)]]
    });
  }

  ngOnInit() {}

  onSubmit() {
  //   console.log('Form submitted', this.form.value);
  //   this.formSubmitted = true;

  //   if (this.form.valid) {
  //     const formData = { ...this.form.value };

  //     console.log('Info', formData);

  //     this.authService.login(formData).subscribe({
  //       next: (response) => {
  //         console.log('Login successful', response);
  //         this.successMessage = 'Connexion réussie !';
  //         this.errorMessage = '';
  //         setTimeout(() => this.router.navigate(['/home']), 3000);
  //       },
  //       error: (error) => {
  //         console.error('Login error', error);
  //         this.errorMessage = 'Erreur lors de la connexion. Veuillez réessayer.';
  //         this.successMessage = '';
  //       }
  //     });
  //   } else {
  //     this.logFormErrors();
  //   }
  }

  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;
    const passwordField: HTMLInputElement = document.getElementById('password') as HTMLInputElement;
    if (this.showPassword) {
      passwordField.type = 'text';
    } else {
      passwordField.type = 'password';
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
}
