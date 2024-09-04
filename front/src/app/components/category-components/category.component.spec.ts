import { ComponentFixture, TestBed } from '@angular/core/testing';
import { CategoryComponent } from './category.component';
import { RouterTestingModule } from '@angular/router/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';
// import { IProduct, ICategory, ICampaign, IImage, IColor, IBrand } from '@interfaces/product.interface';

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

    // Мокируем данные для subcategory перед detectChanges
    // const mockColor: IColor = {
    //   uuid: 'color-uuid',
    //   name: 'Red'
    // };

    // const mockImage: IImage = {
    //   uuid: 'image-uuid',
    //   uri: 'image-uri',
    //   color: mockColor
    // };

    // const mockCampaign: ICampaign = {
    //   id: 'campaign-uuid',
    //   name: 'Test Campaign',
    //   description: 'Campaign description',
    //   discountRate: 15,
    //   dateStart: new Date('2023-01-01'),
    //   dateEnd: new Date('2023-12-31'),
    //   targetUuid: 'prod-uuid',
    //   type: 'PRODUCT'
    // };

    // const mockCategory: ICategory = {
    //   uuid: 'cat-uuid',
    //   name: 'Test Category',
    //   uri: 'category-uri',
    //   description: 'Category description',
    //   images: [mockImage],
    //   subcategories: [],
    //   campaigns: [mockCampaign],
    // };

    // // Добавляем поле uuid в объект brand
    // const mockBrand: IBrand = {
    //   uuid: 'brand-uuid',
    //   name: 'Test Brand'
    // };

    // const mockProduct: IProduct = {
    //   uuid: 'prod-uuid',
    //   name: 'Test Product',
    //   description: 'Product description',
    //   uri: 'product-uri',
    //   price: 100,
    //   stock: 10,
    //   estimatedDelivery: '2-3 days',
    //   weight: 1.5,
    //   height: 10,
    //   width: 5,
    //   category: mockCategory,
    //   subcategory: {
    //     uuid: 'subcat-uuid',
    //     name: 'Test Subcategory',
    //     uri: 'test-uri',
    //     description: 'This is a test description',
    //     category: mockCategory,
    //     products: [],
    //     campaigns: [mockCampaign]
    //   },
    //   brand: mockBrand, // Используем объект mockBrand, который содержит uuid
    //   colors: [mockColor],
    //   images: [mockImage],
    //   stores: [],
    //   campaigns: [mockCampaign],
    //   oldPrice: 120,
    //   newPrice: 100,
    //   discountRate: 15,
    // };

    // component.subcategory = {
    //   uuid: 'subcat-uuid',
    //   name: 'Test Subcategory',
    //   uri: 'test-uri',
    //   description: 'This is a test description',
    //   category: mockCategory,
    //   products: [mockProduct],
    //   campaigns: [mockCampaign]
    // };

    // component.paginatedProducts = component.subcategory.products;

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

//   it('should display product image', () => {
//     const compiled = fixture.nativeElement as HTMLElement;
//     const productImage = compiled.querySelector('img');
//     expect(productImage).toBeTruthy();
//     expect(productImage?.src).toContain(component.paginatedProducts[0]?.images[0]?.uri || '');
//   });

//   it('should display the subcategory name', () => {
//     const compiled = fixture.nativeElement as HTMLElement;
//     const subcategoryTitle = compiled.querySelector('h1');
//     expect(subcategoryTitle?.textContent).toContain(component.subcategory?.name || '');
//   });

//   it('should have a clickable add to cart button', () => {
//     const compiled = fixture.nativeElement as HTMLElement;
//     const addButton = compiled.querySelector('button');
//     expect(addButton).toBeTruthy();
//     expect(addButton?.textContent).toContain('Ajouter au panier');

//     spyOn(component, 'addToCart');
//     addButton?.click();
//     expect(component.addToCart).toHaveBeenCalledWith(component.paginatedProducts[0]);
//   });

//   it('should update the selected price when range input changes', () => {
//     const compiled = fixture.nativeElement as HTMLElement;
//     const priceInput = compiled.querySelector('input[type="range"]') as HTMLInputElement;

//     priceInput.value = '50';
//     priceInput.dispatchEvent(new Event('input'));
//     fixture.detectChanges();

//     expect(component.selectedPrice).toBe(50);
//   });
});
