import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LivraisonComponent } from './livraison.component';

describe('LivraisonComponent', () => {
  let component: LivraisonComponent;
  let fixture: ComponentFixture<LivraisonComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LivraisonComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LivraisonComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
