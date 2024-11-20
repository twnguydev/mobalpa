import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FournisseurComponent } from './fournisseur.component';
import { By } from '@angular/platform-browser';

describe('FournisseurComponent', () => {
  let component: FournisseurComponent;
  let fixture: ComponentFixture<FournisseurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FournisseurComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FournisseurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Vérifie si il les liens des fournisseurs existent', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="https://www.aeg.fr/"]'));
    expect(debugElement).toBeTruthy();
    const debugElement2 = fixture.debugElement.query(By.css('a[href="https://www.bosch-home.fr/"]'));
    expect(debugElement2).toBeTruthy();
    const debugElement3 = fixture.debugElement.query(By.css('a[href="https://www.electrolux.fr/"]'));
    expect(debugElement3).toBeTruthy();
    const debugElement4 = fixture.debugElement.query(By.css('a[href="https://www.gaggenau.com/"]'));
    expect(debugElement4).toBeTruthy();
    const debugElement5 = fixture.debugElement.query(By.css('a[href="https://www.liebherr.com/"]'));
    expect(debugElement5).toBeTruthy();
    const debugElement6 = fixture.debugElement.query(By.css('a[href="https://www.miele.fr/"]'));
    expect(debugElement6).toBeTruthy();
    const debugElement7 = fixture.debugElement.query(By.css('a[href="https://www.siemens-home.bsh-group.com/"]'));
    expect(debugElement7).toBeTruthy();
    const debugElement8 = fixture.debugElement.query(By.css('a[href="https://www.smeg.fr/"]'));
    expect(debugElement8).toBeTruthy();
    const debugElement9 = fixture.debugElement.query(By.css('a[href="https://www.whirlpool.fr/"]'));
    expect(debugElement9).toBeTruthy();
  });
  it('Vérifie si les logos des fournisseurs sont chargés', () => {
  
    const imgElement = fixture.debugElement.query(By.css('img[src="assets/logo/AEG_Logo.png"]'));
    expect(imgElement).toBeTruthy();
    const imgElement2 = fixture.debugElement.query(By.css('img[src="assets/logo/Bosch-Logo.png"]'));
    expect(imgElement2).toBeTruthy();
    const imgElement3 = fixture.debugElement.query(By.css('img[src="assets/logo/Electrolux-Logo.png"]'));
    expect(imgElement3).toBeTruthy();
    const imgElement4 = fixture.debugElement.query(By.css('img[src="assets/logo/gaggenau-logo.png"]'));
    expect(imgElement4).toBeTruthy();
    const imgElement5 = fixture.debugElement.query(By.css('img[src="assets/logo/Liebherr-Logo.png"]'));
    expect(imgElement5).toBeTruthy();
    const imgElement6 = fixture.debugElement.query(By.css('img[src="assets/logo/miele-1-logo.png"]'));
    expect(imgElement6).toBeTruthy();
    const imgElement7 = fixture.debugElement.query(By.css('img[src="assets/logo/Siemens-Logo.png"]'));
    expect(imgElement7).toBeTruthy();
    const imgElement8 = fixture.debugElement.query(By.css('img[src="assets/logo/Smeg-Logo.png"]'));
    expect(imgElement8).toBeTruthy();
    const imgElement9 = fixture.debugElement.query(By.css('img[src="assets/logo/Whirpool-Logo.png"]'));
    expect(imgElement9).toBeTruthy();
});
});
