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
    close: jasmine.createSpy('close')
  };

  const mockDialogData = {
    message: 'Are you sure?'
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

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call onCancel when Annuler button is clicked', () => {
    spyOn(component, 'onCancel');
    const compiled = fixture.nativeElement as HTMLElement;
    const cancelButton = compiled.querySelector('button.bg-gray-200') as HTMLButtonElement;
    expect(cancelButton).toBeTruthy();
    cancelButton.click();
    expect(component.onCancel).toHaveBeenCalled();
  });

  it('should call onConfirm when Confirmer button is clicked', () => {
    spyOn(component, 'onConfirm');
    const compiled = fixture.nativeElement as HTMLElement;
    const confirmButton = compiled.querySelector('button.bg-mobalpa-green') as HTMLButtonElement;
    expect(confirmButton).toBeTruthy();
    confirmButton.click();
    expect(component.onConfirm).toHaveBeenCalled();
  });
});
