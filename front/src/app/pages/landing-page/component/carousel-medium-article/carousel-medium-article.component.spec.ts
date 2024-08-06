import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarouselMediumArticleComponent } from './carousel-medium-article.component';

describe('CarouselMediumArticleComponent', () => {
  let component: CarouselMediumArticleComponent;
  let fixture: ComponentFixture<CarouselMediumArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarouselMediumArticleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarouselMediumArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
