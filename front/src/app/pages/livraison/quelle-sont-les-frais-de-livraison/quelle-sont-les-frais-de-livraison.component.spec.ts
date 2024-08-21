import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuelleSontLesFraisDeLivraisonComponent } from './quelle-sont-les-frais-de-livraison.component';

describe('QuelleSontLesFraisDeLivraisonComponent', () => {
  let component: QuelleSontLesFraisDeLivraisonComponent;
  let fixture: ComponentFixture<QuelleSontLesFraisDeLivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuelleSontLesFraisDeLivraisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuelleSontLesFraisDeLivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
