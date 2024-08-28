import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentRetournerUnProduitComponent } from './comment-retourner-un-produit.component';

describe('CommentRetournerUnProduitComponent', () => {
  let component: CommentRetournerUnProduitComponent;
  let fixture: ComponentFixture<CommentRetournerUnProduitComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentRetournerUnProduitComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommentRetournerUnProduitComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
