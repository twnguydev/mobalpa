import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { LoginComponent } from './login.component';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  beforeEach(async () => {
    const authServiceSpy = jasmine.createSpyObj('AuthService', ['login', 'forgotPassword']);
    const routerSpy = jasmine.createSpyObj('Router', ['navigate']);

    await TestBed.configureTestingModule({
      imports: [ReactiveFormsModule],
      declarations: [LoginComponent],
      providers: [
        { provide: AuthService, useValue: authServiceSpy },
        { provide: Router, useValue: routerSpy }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jasmine.SpyObj<AuthService>;
    router = TestBed.inject(Router) as jasmine.SpyObj<Router>;

    fixture.detectChanges(); // Запускает жизненный цикл Angular и обновляет шаблон компонента
  });

  it('should create the component', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize the form with empty values', () => {
    const form = component.form;
    expect(form).toBeTruthy();
    expect(form.get('email')?.value).toBe('');
    expect(form.get('password')?.value).toBe('');
    expect(form.get('remember')?.value).toBeFalse();
  });

  it('should not submit the form if it is invalid', () => {
    component.onLoginSubmit();
    expect(authService.login).not.toHaveBeenCalled();
  });

  it('should call AuthService.login on valid form submit', () => {
    const mockResponse = { user: { roles: [{ name: 'ROLE_USER' }] } };
    authService.login.and.returnValue(of(mockResponse));

    component.form.setValue({
      email: 'test@example.com',
      password: 'password123',
      remember: true
    });

    component.onLoginSubmit();

    expect(authService.login).toHaveBeenCalledWith('test@example.com', 'password123');
    expect(router.navigate).toHaveBeenCalledWith(['/profil']);
  });

  it('should handle login errors', () => {
    const mockError = { errors: ['Invalid email or password'] };
    authService.login.and.returnValue(throwError(mockError));

    component.form.setValue({
      email: 'test@example.com',
      password: 'wrongpassword',
      remember: true
    });

    component.onLoginSubmit();

    expect(component.errors).toEqual(['Invalid email or password']);
  });

  it('should toggle password visibility', () => {
    component.showPassword = false;
    component.togglePasswordVisibility();
    expect(component.showPassword).toBeTrue();

    component.togglePasswordVisibility();
    expect(component.showPassword).toBeFalse();
  });

  it('should show and hide forgot password form', () => {
    component.showForgotPassword();
    expect(component.forgotPasswordVisible).toBeTrue();

    component.hideForgotPassword();
    expect(component.forgotPasswordVisible).toBeFalse();
  });

  it('should submit forgot password form if valid', () => {
    const mockResponse = { message: 'Email sent' };
    // authService.forgotPassword.and.returnValue(of(mockResponse));

    component.forgotPasswordForm.setValue({ forgotPasswordEmail: 'test@example.com' });
    component.onForgotPassword();

    expect(authService.forgotPassword).toHaveBeenCalledWith('test@example.com');
    expect(component.forgotPasswordVisible).toBeFalse();
  });

  it('should handle forgot password errors', () => {
    const mockError = { error: 'Email not found' };
    authService.forgotPassword.and.returnValue(throwError(mockError));

    component.forgotPasswordForm.setValue({ forgotPasswordEmail: 'test@example.com' });
    component.onForgotPassword();

    expect(component.errorMessage).toBe('Email not found');
  });
});
