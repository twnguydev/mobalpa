import { of } from 'rxjs';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SupportComponent } from './support.component';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';
import { NO_ERRORS_SCHEMA } from '@angular/core';
import { TicketService } from '@services/ticket.service';
import { AuthService } from '@services/auth.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

describe('SupportComponent', () => {
  let component: SupportComponent;
  let fixture: ComponentFixture<SupportComponent>;
  let authService: AuthService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        SupportComponent,
        ReactiveFormsModule,
        FormsModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: { isAuthenticated: () => of(true) } }, // Mock AuthService
        TicketService
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    authService = TestBed.inject(AuthService); // Injection du mock
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SupportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('devrait rendre le champ requestType obligatoire', () => {
    const control = component.supForm.get('requestType');
    control?.setValue('');
    expect(control?.valid).toBeFalsy();
    expect(control?.errors?.['required']).toBeTruthy();
  });
  it('devrait créer le formulaire avec les contrôles nécessaires', () => {
    expect(component.supForm.contains('requestType')).toBeTruthy();
    expect(component.supForm.contains('object')).toBeTruthy();
    expect(component.supForm.contains('requestContent')).toBeTruthy();
  });
  it('devrait rendre le champ object obligatoire et vérifier la longueur minimale', () => {
    const control = component.supForm.get('object');
    control?.setValue('');
    expect(control?.valid).toBeFalsy();
    expect(control?.errors?.['required']).toBeTruthy();

    control?.setValue('1234'); // Moins de 5 caractères
    expect(control?.valid).toBeFalsy();
    expect(control?.errors?.['minlength']).toBeTruthy();

    control?.setValue('Valid Object'); // Au moins 5 caractères
    expect(control?.valid).toBeTruthy();
  });

  it('devrait rendre le champ requestContent obligatoire et vérifier la longueur minimale', () => {
    const control = component.supForm.get('requestContent');
    control?.setValue('');
    expect(control?.valid).toBeFalsy();
    expect(control?.errors?.['required']).toBeTruthy();

    control?.setValue('123456789'); 
    expect(control?.valid).toBeFalsy();
    expect(control?.errors?.['minlength']).toBeTruthy();

    control?.setValue('Valid Request Content');
    expect(control?.valid).toBeTruthy();
  });
  it('devrait désactiver le bouton de soumission si le formulaire est invalide', () => {
    component.selectedTab = 1;
    fixture.detectChanges();

    const formElement: HTMLElement = fixture.nativeElement;
    const submitButton = formElement.querySelector('input.submit-button');

    expect(submitButton).not.toBeNull();

    component.supForm.get('requestType')?.setValue('');
    fixture.detectChanges();
    expect((submitButton as HTMLInputElement).disabled).toBeTruthy();

    component.supForm.get('requestType')?.setValue('1');
    component.supForm.get('object')?.setValue('Objet valide');
    component.supForm.get('requestContent')?.setValue('Contenu valide de la demande');
    fixture.detectChanges();
    expect((submitButton as HTMLInputElement).disabled).toBeFalsy();
  });


  it('devrait afficher un message de succès après la soumission réussie', () => {
    component.selectedTab = 1;  
    fixture.detectChanges();
    component.submissionSuccess = true;
    fixture.detectChanges();

    console.log(fixture.nativeElement.innerHTML);

    const successMessage = fixture.debugElement.query(By.css('.text-green-500'));
    expect(successMessage).not.toBeNull();  

    if (successMessage) {  
      expect(successMessage.nativeElement.textContent).toContain('Votre demande a été envoyée avec succès !');
    }
  });
  it('Verifie l existence du formulaire ', () => {
    expect(component.feedForm).toBeDefined();
    expect(component.feedForm.get('rating')?.value).toBe(''); 
    expect(component.feedForm.get('comment')?.value).toBe('');
  });

  it('dois rendre l espace de notation requis', () => {
    const ratingControl = component.feedForm.get('rating');
    ratingControl?.setValue('');
    expect(ratingControl?.valid).toBeFalsy();
    expect(ratingControl?.errors?.['required']).toBeTruthy();

    ratingControl?.setValue(3);
    expect(ratingControl?.valid).toBeTruthy();
    expect(ratingControl?.errors).toBeNull();
  });

  it('fait en sorte que les l espace commentaire est requis et valide le nombre minimum de caractéres', () => {
    const commentControl = component.feedForm.get('comment');
    commentControl?.setValue('');
    expect(commentControl?.valid).toBeFalsy();
    expect(commentControl?.errors?.['required']).toBeTruthy();

    commentControl?.setValue('short'); // Moins de 10 caractères
    expect(commentControl?.valid).toBeFalsy();
    expect(commentControl?.errors?.['minlength']).toBeTruthy();

    commentControl?.setValue('Valid comment text'); // Au moins 10 caractères
    expect(commentControl?.valid).toBeTruthy();
    expect(commentControl?.errors).toBeNull();
  });

  it('désactive le bouttton submit si le form a des erreurs', () => {
    component.selectedTab = 2; // Sélectionner l'onglet 'Votre avis'
    fixture.detectChanges();

    const formElement: HTMLElement = fixture.nativeElement;
    const submitButton = formElement.querySelector('button[type="submit"]') as HTMLButtonElement;

    expect(submitButton).not.toBeNull();

    component.feedForm.get('rating')?.setValue('');
    component.feedForm.get('comment')?.setValue('');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeTruthy();

    component.feedForm.get('rating')?.setValue(4);
    component.feedForm.get('comment')?.setValue('This is a valid comment');
    fixture.detectChanges();
    expect(submitButton.disabled).toBeFalsy();
  });

  it('affiche les message de succés apres le submit', () => {
    component.selectedTab = 2; 
    fixture.detectChanges();

    component.submissionSuccess = true;
    fixture.detectChanges();

    const successMessage = fixture.debugElement.query(By.css('.text-green-500'));
    expect(successMessage).not.toBeNull();
    if (successMessage) {
      expect(successMessage.nativeElement.textContent).toContain('Merci d\'avoir laissé un avis !');
    }
  });

});
