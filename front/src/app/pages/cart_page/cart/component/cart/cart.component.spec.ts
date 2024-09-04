import { ComponentFixture, TestBed, waitForAsync } from '@angular/core/testing';

import { CartComponent } from './cart.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { By } from '@angular/platform-browser';

describe('CartComponent', () => {
  let component: CartComponent;
  let fixture: ComponentFixture<CartComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CartComponent,HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CartComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
  it('doit rendre l\'input des codes promos', () => {
    const promoCodeInput = fixture.nativeElement.querySelector('input[placeholder="Saisir le code"]');
    expect(promoCodeInput).toBeTruthy();
  });
  it('doit mettre à jour la propriétée couponCode quand le input change', () => {
    const promoCodeInput = fixture.nativeElement.querySelector('input[placeholder="Saisir le code"]');
    promoCodeInput.value = 'PROMO_CODE';
    promoCodeInput.dispatchEvent(new Event('input'));
    fixture.detectChanges();
    expect(component.couponCode).toBe('PROMO_CODE');
  });



  it('doit appeler la fonction checkPromoCode quand le bouton est cliqué', async () => {
    const okButton = fixture.nativeElement.querySelector('button');
    spyOn(component, 'checkPromoCode');
    okButton.click();

    await waitForAsync(() => {
      expect(component.checkPromoCode).toHaveBeenCalledTimes(1);
    })
 });
});
