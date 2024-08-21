import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuelsProduitsSontRetournesSousGarantieComponent } from './quels-produits-sont-retournes-sous-garantie.component';

describe('QuelsProduitsSontRetournesSousGarantieComponent', () => {
  let component: QuelsProduitsSontRetournesSousGarantieComponent;
  let fixture: ComponentFixture<QuelsProduitsSontRetournesSousGarantieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuelsProduitsSontRetournesSousGarantieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuelsProduitsSontRetournesSousGarantieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
