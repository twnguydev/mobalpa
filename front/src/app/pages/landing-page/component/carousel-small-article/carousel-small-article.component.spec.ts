import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CarouselSmallArticleComponent } from './carousel-small-article.component';

describe('CarouselSmallArticleComponent', () => {
  let component: CarouselSmallArticleComponent;
  let fixture: ComponentFixture<CarouselSmallArticleComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CarouselSmallArticleComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CarouselSmallArticleComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
