import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FournisseurComponent } from './fournisseur.component';

describe('FournisseurComponent', () => {
  let component: FournisseurComponent;
  let fixture: ComponentFixture<FournisseurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FournisseurComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FournisseurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
