import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AboutAsComponent } from './about-as.component';

describe('AboutAsComponent', () => {
  let component: AboutAsComponent;
  let fixture: ComponentFixture<AboutAsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AboutAsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AboutAsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
