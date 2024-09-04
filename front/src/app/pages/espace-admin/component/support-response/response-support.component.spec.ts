import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ResponseSupportComponent } from './response-support.component';

describe('ResponseSupportComponent', () => {
  let component: ResponseSupportComponent;
  let fixture: ComponentFixture<ResponseSupportComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ResponseSupportComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ResponseSupportComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
