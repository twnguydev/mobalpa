import { ComponentFixture, TestBed } from '@angular/core/testing';

import { BigCarouselComponent } from './big-carousel.component';
import { By } from '@angular/platform-browser';

describe('BigCarouselComponent', () => {
  let component: BigCarouselComponent;
  let fixture: ComponentFixture<BigCarouselComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [BigCarouselComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(BigCarouselComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('Verifie les liens du gros carousel', () => {
    const debugElement2 = fixture.debugElement.query(By.css('a[href="/categories/petit-electromenager"]'));
    expect(debugElement2).toBeTruthy();
    const debugElement3 = fixture.debugElement.query(By.css('a[href="/categories/grand-electromenager"]'));
    expect(debugElement3).toBeTruthy();
    const debugElement4 = fixture.debugElement.query(By.css('a[href="/categories/gros-electromenager"]'));
    expect(debugElement4).toBeTruthy();
  });
});
