import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommentSePasseUneLivraisonComponent } from './comment-se-passe-une-livraison.component';

describe('CommentSePasseUneLivraisonComponent', () => {
  let component: CommentSePasseUneLivraisonComponent;
  let fixture: ComponentFixture<CommentSePasseUneLivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CommentSePasseUneLivraisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommentSePasseUneLivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
