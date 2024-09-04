import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ConfirmDialogComponent } from './confirm-dialog.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';

describe('ConfirmDialogComponent', () => {
  let component: ConfirmDialogComponent;
  let fixture: ComponentFixture<ConfirmDialogComponent>;

  const mockAuthService = {
    isAuthenticated: jasmine.createSpy('isAuthenticated').and.returnValue(of(true))
  };

  const mockMatDialogRef = {
    close: jasmine.createSpy('close') // Мокируем метод close для закрытия диалога
  };

  const mockDialogData = {
    message: 'Are you sure?' // Мокируем данные, передаваемые в диалог
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [
        ConfirmDialogComponent,
      ],
      imports: [
        RouterTestingModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService },
        { provide: MatDialogRef, useValue: mockMatDialogRef },
        { provide: MAT_DIALOG_DATA, useValue: mockDialogData }
      ]
    })
      .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ConfirmDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  // Тест на создание компонента
  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Тест на кликабельность кнопки Annuler и вызов onCancel()
  it('should call onCancel when Annuler button is clicked', () => {
    spyOn(component, 'onCancel'); // Создаем spy на метод onCancel
    const compiled = fixture.nativeElement as HTMLElement;
    const cancelButton = compiled.querySelector('button.bg-gray-200') as HTMLButtonElement; // Находим кнопку Annuler
    expect(cancelButton).toBeTruthy(); // Убеждаемся, что кнопка существует
    cancelButton.click(); // Эмулируем клик
    expect(component.onCancel).toHaveBeenCalled(); // Проверяем, что onCancel был вызван
  });

  // Тест на кликабельность кнопки Confirmer и вызов onConfirm()
  it('should call onConfirm when Confirmer button is clicked', () => {
    spyOn(component, 'onConfirm'); // Создаем spy на метод onConfirm
    const compiled = fixture.nativeElement as HTMLElement;
    const confirmButton = compiled.querySelector('button.bg-mobalpa-green') as HTMLButtonElement; // Находим кнопку Confirmer
    expect(confirmButton).toBeTruthy(); // Убеждаемся, что кнопка существует
    confirmButton.click(); // Эмулируем клик
    expect(component.onConfirm).toHaveBeenCalled(); // Проверяем, что onConfirm был вызван
  });
});
