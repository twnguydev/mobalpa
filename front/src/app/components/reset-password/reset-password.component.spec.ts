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

  it('should call onSubmit when "Réinitialiser mon mot de passe" button is clicked', () => {
    spyOn(component, 'onSubmit');
    const compiled = fixture.nativeElement as HTMLElement;
    const submitButton = compiled.querySelector('button[type="submit"]') as HTMLButtonElement;
    
    expect(submitButton).toBeTruthy();
    submitButton.click();
    expect(component.onSubmit).toHaveBeenCalled();
  });


  it('should show error message when there is an error', () => {
    mockAuthService.resetPassword.and.returnValue(of({ error: 'Reset password failed' }));
    component.errorMessage = 'Reset password failed';
    
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const errorMessage = compiled.querySelector('.text-red-600') as HTMLElement;

    expect(errorMessage).toBeTruthy();
    expect(errorMessage.textContent).toContain('Reset password failed');
  });
});
