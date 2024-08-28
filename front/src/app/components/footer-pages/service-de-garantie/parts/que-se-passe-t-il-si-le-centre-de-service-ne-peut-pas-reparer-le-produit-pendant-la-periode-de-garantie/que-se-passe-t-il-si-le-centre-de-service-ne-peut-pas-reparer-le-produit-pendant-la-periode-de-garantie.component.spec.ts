import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent } from './que-se-passe-t-il-si-le-centre-de-service-ne-peut-pas-reparer-le-produit-pendant-la-periode-de-garantie.component';

describe('QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent', () => {
  let component: QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent;
  let fixture: ComponentFixture<QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
