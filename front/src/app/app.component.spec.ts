import { TestBed } from '@angular/core/testing';
import { AppComponent } from './app.component';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';

describe('AppComponent', () => {
  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AppComponent],
    }).compileComponents();
  });

  it('should create the app', () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app).toBeTruthy();
  });

  it("should have the title", () => {
    const fixture = TestBed.createComponent(AppComponent);
    const app = fixture.componentInstance;
    expect(app.title).toEqual('Mobalpa & Archidéco - Spécialiste de la cuisine Cuisine et l Éctroménager');
  });


  it("devrait charger les routes", () => {
    const fixture = TestBed.createComponent(AppComponent);

    const element5: DebugElement = fixture.debugElement.query(By.css('router-outlet'));
    expect(element5).toBeTruthy();
  });
  it("devrait afficher le header et le footer", () => {
    const fixture = TestBed.createComponent(AppComponent);

    const element5: DebugElement = fixture.debugElement.query(By.css('app-main-header'));
    expect(element5).toBeTruthy();
    const element6: DebugElement = fixture.debugElement.query(By.css('app-footer'));
    expect(element6).toBeTruthy();
  });
});
