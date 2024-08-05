import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCategorieOnPageComponent } from './show-categorie-on-page.component';

describe('ShowCategorieOnPageComponent', () => {
  let component: ShowCategorieOnPageComponent;
  let fixture: ComponentFixture<ShowCategorieOnPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCategorieOnPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCategorieOnPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
