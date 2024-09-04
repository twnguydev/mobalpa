import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoryComponent } from './category.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';

describe('CategoryComponent', () => {
  let component: CategoryComponent;
  let fixture: ComponentFixture<CategoryComponent>;

  const mockAuthService = {
    isAuthenticated: jasmine.createSpy('isAuthenticated').and.returnValue(of(true))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        CategoryComponent,
        RouterTestingModule,
        HttpClientTestingModule
      ],
      providers: [
        { provide: AuthService, useValue: mockAuthService }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(CategoryComponent);
    component = fixture.componentInstance;

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should display the correct number of found products', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    const productCountText = compiled.querySelector('.flex .text-gray-700')?.textContent;
    expect(productCountText).toContain(`${component.filteredProducts.length} article`);
  });

  // it('should have a clickable add to cart button', () => {
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   const addButton = compiled.querySelector('button');
  //   expect(addButton).toBeTruthy();
  //   expect(addButton?.textContent).toContain('Ajouter au panier');

  //   spyOn(component, 'addToCart');
  //   addButton?.click();
  //   expect(component.addToCart).toHaveBeenCalledWith(component.paginatedProducts[0]);
  // });

  // it('should update the selected price when range input changes', () => {
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   const priceInput = compiled.querySelector('input[type="range"]') as HTMLInputElement;

  //   priceInput.value = '50';
  //   priceInput.dispatchEvent(new Event('input'));
  //   fixture.detectChanges();

  //   expect(component.selectedPrice).toBe(50);
  // });

    // it('should display product image', () => {
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   const productImage = compiled.querySelector('img');
  //   expect(productImage).toBeTruthy();
  //   expect(productImage?.src).toContain(component.paginatedProducts[0]?.images[0]?.uri || '');
  // });

  // it('should display the subcategory name', () => {
  //   const compiled = fixture.nativeElement as HTMLElement;
  //   const subcategoryTitle = compiled.querySelector('h1');
  //   expect(subcategoryTitle?.textContent).toContain(component.subcategory?.name || '');
  // });
});
