import { ComponentFixture, TestBed } from '@angular/core/testing';

import { QuellesSontLesMethodesDeLivraisonComponent } from './quelles-sont-les-methodes-de-livraison.component';

describe('QuellesSontLesMethodesDeLivraisonComponent', () => {
  let component: QuellesSontLesMethodesDeLivraisonComponent;
  let fixture: ComponentFixture<QuellesSontLesMethodesDeLivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [QuellesSontLesMethodesDeLivraisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(QuellesSontLesMethodesDeLivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
