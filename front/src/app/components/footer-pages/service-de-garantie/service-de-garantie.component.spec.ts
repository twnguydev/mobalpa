import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ServiceDeGarantieComponent } from './service-de-garantie.component';

describe('ServiceDeGarantieComponent', () => {
  let component: ServiceDeGarantieComponent;
  let fixture: ComponentFixture<ServiceDeGarantieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ServiceDeGarantieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ServiceDeGarantieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
