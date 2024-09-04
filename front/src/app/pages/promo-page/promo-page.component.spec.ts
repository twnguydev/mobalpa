import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RouterTestingModule } from '@angular/router/testing';

import { PromoPageComponent } from './promo-page.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('PromoPageComponent', () => {
  let component: PromoPageComponent;
  let fixture: ComponentFixture<PromoPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PromoPageComponent, HttpClientTestingModule, RouterTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PromoPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
