import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PromoComponent } from './promo.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('PromoComponent', () => {
  let component: PromoComponent;
  let fixture: ComponentFixture<PromoComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromoComponent,HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PromoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
