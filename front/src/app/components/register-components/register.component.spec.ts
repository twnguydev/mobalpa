import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RegisterComponent } from './register.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { of, throwError } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { Router } from '@angular/router';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  const mockAuthService = jasmine.createSpyObj('AuthService', ['signup']);
  const mockRouter = jasmine.createSpyObj('Router', ['navigate']);

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RegisterComponent,
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: Router, useValue: mockRouter }
      ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call onSubmit when "S\'inscrire" button is clicked', () => {
    spyOn(component, 'onSubmit');

    const compiled = fixture.nativeElement as HTMLElement;
    const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
    
    expect(submitButton).toBeTruthy();

    submitButton.click();

    expect(component.onSubmit).toHaveBeenCalled();
  });

  it('should show success message on successful registration', () => {
    mockAuthService.signup.and.returnValue(of({}));

    component.form.setValue({
      lastname: 'Test',
      firstname: 'User',
      email: 'test@example.com',
      phoneNumber: '0123456789',
      birthdate: '1990-01-01',
      password: 'password123',
      confirmPassword: 'password123',
      terms: true,
      communications: false
    });

    component.onSubmit();

    fixture.detectChanges();

    expect(mockAuthService.signup).toHaveBeenCalledWith(component.form.value);

    const compiled = fixture.nativeElement as HTMLElement;
    const successMessage = compiled.querySelector('.text-green-600') as HTMLElement;
    expect(successMessage).toBeTruthy();
    expect(successMessage.textContent).toContain('Inscription réussie ! Vous allez être redirigé vers la page de connexion');

    setTimeout(() => {
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/auth/connexion']);
    }, 5000);
  });
});
