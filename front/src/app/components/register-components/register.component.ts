import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators, AbstractControl } from '@angular/forms';
import { AuthService } from './../../../services/auth.service';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';

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

  constructor(private fb: FormBuilder, private authService: AuthService, private router: Router) {
    this.form = this.fb.group({
      lastname: ['', Validators.required],
      firstname: ['', Validators.required],
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

  passwordMatchValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const passwordControl = control.get('password');
    const confirmPasswordControl = control.get('confirmPassword');

    if (!passwordControl || !confirmPasswordControl) {
      return null;
    }

    return passwordControl.value === confirmPasswordControl.value ? null : { passwordMismatch: true };
  }

  onSubmit() {
    this.formSubmitted = true;
    if (this.form.valid) {
      const formData = {
        ...this.form.value,
        address: null,
        city: null,
        zipcode: null
      };

      this.authService.signup(formData).subscribe({
        next: (response) => {
          this.successMessage = 'Inscription réussie !';
          this.errorMessage = '';
          setTimeout(() => this.router.navigate(['/auth/connexion']), 6000);
        },
        error: (error) => {
          console.error('Signup error', error);
          this.handleErrorResponse(error);
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

  handleErrorResponse(error: any) {
    if (error.status === 400) {
      this.errorMessage = 'Les données fournies sont invalides. Veuillez vérifier les champs et réessayer.';
    } else if (error.status === 409) {
      this.errorMessage = 'Cet e-mail est déjà utilisé. Veuillez en utiliser un autre.';
    } else {
      this.errorMessage = 'Erreur lors de l\'inscription. Veuillez réessayer.';
    }
    this.successMessage = '';
  }
}
