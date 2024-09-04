import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ConfirmOrderComponent } from './confirm-order.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('DeliveryComponent', () => {
  let component: ConfirmOrderComponent;
  let fixture: ComponentFixture<ConfirmOrderComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ConfirmOrderComponent,HttpClientTestingModule]
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