import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BigCarouselComponent } from './big-carousel.component';

describe('BigCarouselComponent', () => {
  let component: BigCarouselComponent;
  let fixture: ComponentFixture<BigCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BigCarouselComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BigCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
