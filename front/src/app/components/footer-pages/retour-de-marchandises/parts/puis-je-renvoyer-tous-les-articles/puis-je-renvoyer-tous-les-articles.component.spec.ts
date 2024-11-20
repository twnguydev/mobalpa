import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PuisJeRenvoyerTousLesArticlesComponent } from './puis-je-renvoyer-tous-les-articles.component';

describe('PuisJeRenvoyerTousLesArticlesComponent', () => {
  let component: PuisJeRenvoyerTousLesArticlesComponent;
  let fixture: ComponentFixture<PuisJeRenvoyerTousLesArticlesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PuisJeRenvoyerTousLesArticlesComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PuisJeRenvoyerTousLesArticlesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
