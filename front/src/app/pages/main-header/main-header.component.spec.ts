import { ComponentFixture, TestBed } from '@angular/core/testing';

import { MainHeaderComponent } from './main-header.component';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('MainHeaderComponent', () => {
  let component: MainHeaderComponent;
  let fixture: ComponentFixture<MainHeaderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [MainHeaderComponent, HttpClientTestingModule]
    })
      .compileComponents();

    fixture = TestBed.createComponent(MainHeaderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    
  });

  it('verifie les liens de la page d\'accueil', () => {
    const debugElement1 = fixture.debugElement.query(By.css('a[href="/"]'));
    expect(debugElement1).toBeTruthy();
  });
  it('verifie les liens de la page nouveauté', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/nouveaute"]'));
    expect(debugElement2).toBeTruthy();
  });
  it('verifie les liens de la page panier', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/panier"]'));
    expect(debugElement2).toBeTruthy();
  });
  it('verifie les liens de la page liste de souhaits', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/liste-de-souhaits"]'));
    expect(debugElement2).toBeTruthy();
  });
  it('verifie les liens de la page profil', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const linkElement = compiled.querySelector('a[href="/profil"]');
    expect(linkElement).toBeTruthy();
  });
  it('verifie les liens de la page de connexion ', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/auth/connexion"]'));
    expect(debugElement2).toBeTruthy();
  });
  it('verifie les liens de la page d\'inscription ', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/auth/connexion"]'));
    expect(debugElement2).toBeTruthy();
  });
});


