import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SousCategorieComponent } from './sous-categorie.component';

describe('SousCategorieComponent', () => {
  let component: SousCategorieComponent;
  let fixture: ComponentFixture<SousCategorieComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SousCategorieComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SousCategorieComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
