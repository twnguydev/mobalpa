import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ResetPasswordComponent } from './reset-password.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AuthService } from '@services/auth.service';
import { of } from 'rxjs';

describe('ResetPasswordComponent', () => {
  let component: ResetPasswordComponent;
  let fixture: ComponentFixture<ResetPasswordComponent>;

  const mockAuthService = {
    isAuthenticated: jasmine.createSpy('isAuthenticated').and.returnValue(of(true)),
    currentUser$: of({ id: 1, name: 'Test User' }),
    resetPassword: jasmine.createSpy('resetPassword').and.returnValue(of({ message: 'Password reset successful' }))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ResetPasswordComponent,
        RouterTestingModule,
        HttpClientTestingModule,
        FormsModule,
        ReactiveFormsModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService }
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResetPasswordComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Тест на кликабельность кнопки "Réinitialiser mon mot de passe"
  it('should call onSubmit when "Réinitialiser mon mot de passe" button is clicked', () => {
    spyOn(component, 'onSubmit'); // Отслеживаем вызов onSubmit
    const compiled = fixture.nativeElement as HTMLElement;
    const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
    
    expect(submitButton).toBeTruthy(); // Проверяем, что кнопка существует
    submitButton.click(); // Кликаем по кнопке
    expect(component.onSubmit).toHaveBeenCalled(); // Проверяем, что onSubmit был вызван
  });

  // Тест на отображение ошибки
  it('should show error message when there is an error', () => {
    // Симулируем ошибку при сбросе пароля
    mockAuthService.resetPassword.and.returnValue(of({ error: 'Reset password failed' }));
    component.errorMessage = 'Reset password failed';
    
    fixture.detectChanges(); // Обновляем отображение после установки errorMessage

    const compiled = fixture.nativeElement as HTMLElement;
    const errorMessage = compiled.querySelector('.text-red-600') as HTMLElement;

    expect(errorMessage).toBeTruthy(); // Проверяем, что сообщение об ошибке отображается
    expect(errorMessage.textContent).toContain('Reset password failed');
  });
});
