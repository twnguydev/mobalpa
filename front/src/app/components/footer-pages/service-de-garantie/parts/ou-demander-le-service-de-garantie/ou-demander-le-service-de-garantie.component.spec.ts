import { ComponentFixture, TestBed } from '@angular/core/testing';

import { OuDemanderLeServiceDeGarantieComponent } from './ou-demander-le-service-de-garantie.component';

describe('OuDemanderLeServiceDeGarantieComponent', () => {
  let component: OuDemanderLeServiceDeGarantieComponent;
  let fixture: ComponentFixture<OuDemanderLeServiceDeGarantieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [OuDemanderLeServiceDeGarantieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(OuDemanderLeServiceDeGarantieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
