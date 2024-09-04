import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FooterComponent } from './footer.component';
import { RouterLinkWithHref } from '@angular/router';
import { By } from '@angular/platform-browser';

describe('FooterComponent', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Vérifie si il y a une route "/qui-sommes-nous"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/qui-sommes-nous"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/conditions-generales-utilisation"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/conditions-generales-utilisation"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "jobs.fourniergroupe.com"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="https://jobs.fourniergroupe.com/"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/contactez-nous"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/contactez-nous"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/livraison"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/livraison"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/service-de-garantie"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/service-de-garantie"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/retour-de-marchandises"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/retour-de-marchandises"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/support"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/support"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/cooperation"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/cooperation"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "/fournisseur"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="/fournisseur"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Vérifie si il y a une route "www.mobalpa.fr/franchise"', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="https://www.mobalpa.fr/franchise"]'));
    expect(debugElement).toBeTruthy();
  });

  it('Vérifie si les images sont chargées', () => {
    const imgElement = fixture.debugElement.query(By.css('img[src="assets/footer-icon/mini-logo-MA3.png"]'));
    expect(imgElement).toBeTruthy();
    const imgElement2 = fixture.debugElement.query(By.css('img[src="assets/footer-icon/instagram_1.png"]'));
    expect(imgElement2).toBeTruthy();
    const imgElement3 = fixture.debugElement.query(By.css('img[src="assets/footer-icon/twitter_.png"]'));
    expect(imgElement3).toBeTruthy();
    const imgElement4 = fixture.debugElement.query(By.css('img[src="assets/footer-icon/facebook-circular-logo.png'));
    expect(imgElement4).toBeTruthy();
    const imgElement5 = fixture.debugElement.query(By.css('img[src="assets/footer-icon/youtube.png'));
    expect(imgElement5).toBeTruthy();
    const imgElement6 = fixture.debugElement.query(By.css('img[src="assets/footer-icon/whatsapp.png'));
    expect(imgElement6).toBeTruthy();
  });
})
