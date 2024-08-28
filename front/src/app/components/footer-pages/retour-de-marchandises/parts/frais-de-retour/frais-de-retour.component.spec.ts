import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FraisDeRetourComponent } from './frais-de-retour.component';

describe('FraisDeRetourComponent', () => {
  let component: FraisDeRetourComponent;
  let fixture: ComponentFixture<FraisDeRetourComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FraisDeRetourComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FraisDeRetourComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
