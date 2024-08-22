import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AnnulerCommandeEnCoursDeLivraisonComponent } from './annuler-commande-en-cours-de-livraison.component';

describe('AnnulerCommandeEnCoursDeLivraisonComponent', () => {
  let component: AnnulerCommandeEnCoursDeLivraisonComponent;
  let fixture: ComponentFixture<AnnulerCommandeEnCoursDeLivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AnnulerCommandeEnCoursDeLivraisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AnnulerCommandeEnCoursDeLivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
