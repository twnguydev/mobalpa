import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ContactPageComponent } from './contact-page.component';
import { By } from '@angular/platform-browser';

describe('ContactPageComponent', () => {
  let component: ContactPageComponent;
  let fixture: ComponentFixture<ContactPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ContactPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ContactPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Verifie si la redirection pour les malentendants fonctionne', () => {
    const debugElement = fixture.debugElement.query(By.css('a[href="https://mobalpa.elioz.fr/3.4/?hash=1595c933fcbc4999e228eb753fe73f10"]'));
    expect(debugElement).toBeTruthy();
  });
  it('Verifie l\'icone est chargée', () => {
    const imgElement = fixture.debugElement.query(By.css('img[src="assets/icon/telephone.png"]'));
    expect(imgElement).toBeTruthy();
  });
});
