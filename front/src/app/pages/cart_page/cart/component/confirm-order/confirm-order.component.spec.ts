import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmOrderComponent } from './confirm-order.component';

describe('DeliveryComponent', () => {
  let component: ConfirmOrderComponent;
  let fixture: ComponentFixture<ConfirmOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmOrderComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ConfirmOrderComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
