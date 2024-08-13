import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SubcategoriePageComponent } from './subcategorie-page.component';

describe('SubcategoriePageComponent', () => {
  let component: SubcategoriePageComponent;
  let fixture: ComponentFixture<SubcategoriePageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SubcategoriePageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SubcategoriePageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
