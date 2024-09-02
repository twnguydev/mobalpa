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

  it('Verifie les liens cliquables', () => {
    spyOn(component, 'testLink')
    let link = fixture.nativeElement.querySelector('a');
    link.click();
    expect(component).toBeTruthy();
  });
});
