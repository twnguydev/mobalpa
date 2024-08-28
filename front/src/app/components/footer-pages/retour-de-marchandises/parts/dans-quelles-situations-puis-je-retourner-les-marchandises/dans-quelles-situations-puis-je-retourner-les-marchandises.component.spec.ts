import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent } from './dans-quelles-situations-puis-je-retourner-les-marchandises.component';

describe('DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent', () => {
  let component: DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent;
  let fixture: ComponentFixture<DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
