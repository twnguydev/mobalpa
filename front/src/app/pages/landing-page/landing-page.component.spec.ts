import { ComponentFixture, TestBed } from '@angular/core/testing';

import { LandingPageComponent } from './landing-page.component';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('LandingPageComponent', () => {
  let component: LandingPageComponent;
  let fixture: ComponentFixture<LandingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [LandingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(LandingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('verifie si au moins 1 component est chargée dans le component principale ', () => { 
    const element: DebugElement = fixture.debugElement.query(By.css('app-big-carousel'));
    expect(element).toBeTruthy();
  });
});
