import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CooperationComponent } from './cooperation.component';

describe('CooperationComponent', () => {
  let component: CooperationComponent;
  let fixture: ComponentFixture<CooperationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CooperationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CooperationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
