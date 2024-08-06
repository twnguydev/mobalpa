import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ArticleMedium } from './article-medium.component';

describe('ArticleMedium', () => {
  let component: ArticleMedium;
  let fixture: ComponentFixture<ArticleMedium>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ArticleMedium]
    })
      .compileComponents();

    fixture = TestBed.createComponent(ArticleMedium);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
