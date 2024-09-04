import { ComponentFixture, TestBed } from '@angular/core/testing';
import { LoginComponent } from './login.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { AuthService } from '@services/auth.service';
import { of, throwError } from 'rxjs';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jasmine.SpyObj<AuthService>;
  let router: jasmine.SpyObj<Router>;

  // Замокированный объект localStorage
  const mockLocalStorage = {
    store: {} as { [key: string]: string },
    getItem(key: string): string | null {
      return this.store[key] || null;
    },
    setItem(key: string, value: string): void {
      this.store[key] = value;
    },
    removeItem(key: string): void {
      delete this.store[key];
    },
    clear(): void {
      this.store = {};
    }
  };

  beforeEach(async () => {
    authService = jasmine.createSpyObj('AuthService', ['login', 'forgotPassword']);
    router = jasmine.createSpyObj('Router', ['navigate']);

    // Подменяем поведение window.localStorage
    Object.defineProperty(window, 'localStorage', {
      value: mockLocalStorage
    });

    await TestBed.configureTestingModule({
      imports: [LoginComponent, HttpClientTestingModule, ReactiveFormsModule],
      providers: [
        { provide: AuthService, useValue: authService },
        { provide: Router, useValue: router }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Тест кликабельности кнопки логина
  it('should call onLoginSubmit when "Se connecter" button is clicked', () => {
    spyOn(component, 'onLoginSubmit'); // Отслеживаем вызов метода onLoginSubmit
  
    const compiled = fixture.nativeElement as HTMLElement;
    
    // Находим кнопку для отправки формы
    const loginButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
  
    expect(loginButton).toBeTruthy(); // Проверяем, что кнопка существует
  
    // Эмулируем клик по кнопке
    loginButton.click();
  
    fixture.detectChanges(); // Обновляем DOM после клика
  
    expect(component.onLoginSubmit).toHaveBeenCalled(); // Проверяем, что onLoginSubmit был вызван
  });
  

  // Тест: проверка, что форма валидная при заполнении правильных данных
  it('should make the form valid when correct email and password are provided', () => {
    component.form.setValue({
      email: 'test@example.com',
      password: 'password123',
      remember: true
    });

    expect(component.form.valid).toBeTrue(); // Проверяем, что форма валидна
  });

  // Тест: проверка, что форма невалидная при неправильных данных
  it('should make the form invalid when email is missing', () => {
    component.form.setValue({
      email: '',
      password: 'password123',
      remember: true
    });

    expect(component.form.invalid).toBeTrue(); // Проверяем, что форма невалидна
  });

  // Тест: Успешная отправка формы логина
  it('should navigate to profile on successful login', () => {
    authService.login.and.returnValue(of({ user: { roles: [{ name: 'ROLE_USER' }] } }));

    component.form.setValue({
      email: 'test@example.com',
      password: 'password123',
      remember: true
    });

    component.onLoginSubmit(); // Отправляем форму

    expect(authService.login).toHaveBeenCalledWith('test@example.com', 'password123');
    expect(router.navigate).toHaveBeenCalledWith(['/profil']);
  });

  // Тест: Ошибка при логине
  it('should display error message when login fails', () => {
    const mockError = { errors: ['Invalid credentials'] };
    authService.login.and.returnValue(throwError(mockError));

    component.form.setValue({
      email: 'test@example.com',
      password: 'wrongpassword',
      remember: false
    });

    component.onLoginSubmit(); // Отправляем форму

    fixture.detectChanges(); // Обновляем DOM

    const errorElement = fixture.nativeElement.querySelector('.bg-red-100');
    expect(errorElement).toBeTruthy(); // Проверяем, что ошибка отображается
    expect(errorElement.textContent).toContain('Invalid credentials');
  });

  // Тест кликабельности кнопки "Mot de passe oublié"
  it('should call showForgotPassword when "Mot de passe oublié" button is clicked', () => {
    spyOn(component, 'showForgotPassword'); // Отслеживаем вызов метода showForgotPassword
  
    const compiled = fixture.nativeElement as HTMLElement;
  
    // Убедитесь, что кнопка найдена корректно
    // const forgotPasswordButton = compiled.querySelector('button[text="Mot de passe oublié ?"]') as HTMLButtonElement;
    const forgotPasswordButton = compiled.querySelector('button[type="button"]') as HTMLButtonElement;

  
    expect(forgotPasswordButton).toBeTruthy(); // Проверяем, что кнопка существует
  
    forgotPasswordButton.click(); // Эмулируем клик
  
    fixture.detectChanges(); // Обновляем DOM после клика
  
    expect(component.showForgotPassword).toHaveBeenCalled(); // Проверяем, что метод showForgotPassword был вызван
  });
  

  // Тест отправки формы восстановления пароля
  it('should send forgot password request and hide form on success', () => {
    // Исправлено: возвращаем строку вместо объекта
    authService.forgotPassword.and.returnValue(of('Password reset email sent'));

    component.showForgotPassword();
    fixture.detectChanges();

    component.forgotPasswordForm.setValue({
      forgotPasswordEmail: 'test@example.com'
    });

    component.onForgotPassword(); // Отправляем форму

    expect(authService.forgotPassword).toHaveBeenCalledWith('test@example.com');
    expect(component.forgotPasswordVisible).toBeFalse(); // Проверяем, что форма скрыта
  });

  // Тест обработки ошибок при восстановлении пароля
  it('should show error message when forgot password fails', () => {
    authService.forgotPassword.and.returnValue(throwError({ error: 'Email not found' }));

    component.showForgotPassword();
    fixture.detectChanges();

    component.forgotPasswordForm.setValue({
      forgotPasswordEmail: 'test@example.com'
    });

    component.onForgotPassword(); // Отправляем форму

    fixture.detectChanges(); // Обновляем DOM

    const errorElement = fixture.nativeElement.querySelector('.text-red-600');
    expect(errorElement).toBeTruthy(); // Проверяем, что ошибка отображается
    expect(errorElement.textContent).toContain('Email not found');
  });
});
