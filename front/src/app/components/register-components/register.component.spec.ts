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

  // Тест кликабельности кнопки
  it('should call onSubmit when "S\'inscrire" button is clicked', () => {
    // Отслеживаем вызов метода onSubmit
    spyOn(component, 'onSubmit');

    // Находим кнопку в DOM
    const compiled = fixture.nativeElement as HTMLElement;
    const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
    
    expect(submitButton).toBeTruthy(); // Проверяем, что кнопка существует

    // Эмулируем клик по кнопке
    submitButton.click();
    
    // Проверяем, что метод onSubmit был вызван
    expect(component.onSubmit).toHaveBeenCalled();
  });

  // Тест: Успешная отправка формы
  it('should show success message on successful registration', () => {
    // Мокаем успешный ответ сервиса
    mockAuthService.signup.and.returnValue(of({}));

    // Заполняем все поля формы
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

    component.onSubmit(); // Отправляем форму

    fixture.detectChanges(); // Обновляем DOM после отправки

    // Проверяем, что signup вызван
    expect(mockAuthService.signup).toHaveBeenCalledWith(component.form.value);

    // Проверяем, что сообщение об успехе отображается
    const compiled = fixture.nativeElement as HTMLElement;
    const successMessage = compiled.querySelector('.text-green-600') as HTMLElement;
    expect(successMessage).toBeTruthy(); // Проверяем, что сообщение об успехе отображается
    expect(successMessage.textContent).toContain('Inscription réussie ! Vous allez être redirigé vers la page de connexion');

    // Проверяем, что произошло перенаправление
    setTimeout(() => {
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/auth/connexion']);
    }, 5000);
  });
});
