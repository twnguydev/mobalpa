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
    const element2: DebugElement = fixture.debugElement.query(By.css('app-promo'));
    expect(element2).toBeTruthy();
    const element3: DebugElement = fixture.debugElement.query(By.css('app-carousel-medium-article'));
    expect(element3).toBeTruthy();
    const element4: DebugElement = fixture.debugElement.query(By.css('app-show-categorie-on-page'));
    expect(element4).toBeTruthy();
    const element5: DebugElement = fixture.debugElement.query(By.css('app-banner'));
    expect(element5).toBeTruthy();
    const element6: DebugElement = fixture.debugElement.query(By.css('app-notice'));
    expect(element6).toBeTruthy();
  });
});
