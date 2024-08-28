import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReparationSousGarantieComponent } from './reparation-sous-garantie.component';

describe('ReparationSousGarantieComponent', () => {
  let component: ReparationSousGarantieComponent;
  let fixture: ComponentFixture<ReparationSousGarantieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReparationSousGarantieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ReparationSousGarantieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
