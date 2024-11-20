import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DansQuelsCasLaGarantieNEstEllePasAssureeComponent } from './dans-quels-cas-la-garantie-n-est-elle-pas-assuree.component';

describe('DansQuelsCasLaGarantieNEstEllePasAssureeComponent', () => {
  let component: DansQuelsCasLaGarantieNEstEllePasAssureeComponent;
  let fixture: ComponentFixture<DansQuelsCasLaGarantieNEstEllePasAssureeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DansQuelsCasLaGarantieNEstEllePasAssureeComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DansQuelsCasLaGarantieNEstEllePasAssureeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
