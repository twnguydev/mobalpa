import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { ProductComponent } from './product.component';
import { ProductService } from '@services/product.service';
import { UserService } from '@services/user.service';
import { SatisfactionService } from '@services/satisfaction.service';
import { AuthService } from '@services/auth.service';
import { of } from 'rxjs';
import { IProduct, IColor, ICampaign } from '@interfaces/product.interface';

describe('ProductComponent', () => {
  let component: ProductComponent;
  let fixture: ComponentFixture<ProductComponent>;
  let productService: ProductService;
  let userService: UserService;
  let satisfactionService: SatisfactionService;
  let authService: AuthService;

  const mockProduct: IProduct = {
    uuid: '123',
    name: 'Test Product',
    description: 'Test Description',
    uri: 'test-product-uri',
    price: 100,
    stock: 10,
    estimatedDelivery: new Date().toISOString(),
    weight: 1,
    height: 10,
    width: 20,

    category: {
      uuid: 'category-uuid',
      name: 'Test Category',
      uri: 'category-uri',
      description: 'Category Description',
      images: [
      { uuid: 'image-uuid-cat', uri: 'category-image.jpg', color: { uuid: 'color-uuid-red', name: 'Red' } }
      ],
      subcategories: [
      {
          uuid: 'subcategory-uuid',
          name: 'Test Subcategory',
          uri: 'subcategory-uri',
          description: 'Subcategory Description',
          category: {
            uuid: 'category-uuid',
            name: 'Test Category',
            uri: 'category-uri',
            description: 'Category Description',
            images: [],
            subcategories: [],
            campaigns: []
          },
          products: [],
          campaigns: []
        }
      ],
      isOpen: true,
      campaigns: [
 {
          id: 'campaign-uuid',
          name: 'Test Campaign',
          discountRate: 10,
          dateStart: new Date('2024-01-01'),
          dateEnd: new Date('2024-12-31'),
          targetUuid: '123',
          type: 'PRODUCT'
        }
      ]
    },
    subcategory: {
      uuid: 'subcategory-uuid',
      name: 'Test Subcategory',
      uri: 'subcategory-uri',
      description: 'Subcategory Description',
      category: {
        uuid: 'category-uuid',
        name: 'Test Category',
        uri: 'category-uri',
        description: 'Category Description',
        images: [],
        subcategories: [],
        campaigns: []
      },
      products: [],
      campaigns: []
    },
    brand: {
      uuid: 'brand-uuid',
      name: 'Test Brand'
    },
    colors: [ { uuid: 'color-uuid-red', name: 'Red' }, { uuid: 'color-uuid-blue', name: 'Blue' }
    ],
    images: [ { uuid: 'image-uuid-1', uri: 'test-image-1.jpg', color: { uuid: 'color-uuid-red', name: 'Red' } }, { uuid: 'image-uuid-2', uri: 'test-image-2.jpg', color: { uuid: 'color-uuid-blue', name: 'Blue' } }
    ],
    stores: [ {
        uuid: 'store-uuid',
        name: 'Test Store',
        address: '123 Test St.',
        phoneNumber: '123-456-7890',
        email: 'store@test.com',
        openingHours: '9 AM - 9 PM',
        products: []
      }
    ],
    campaigns: [ {
        id: 'campaign-uuid',
        name: 'Test Campaign',
        discountRate: 10,
        dateStart: new Date('2024-01-01'),
        dateEnd: new Date('2024-12-31'),
        targetUuid: '123',
        type: 'PRODUCT'
      }
    ],

    oldPrice: 120,
    newPrice: 90,
    discountRate: 10
  };


  const mockReviews = [ {
      userUuid: { firstname: 'John', lastname: 'Doe' },
      createdAt: new Date().toISOString(),
      rating: 4,
      comment: 'Good product'
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ProductComponent],
      imports: [ReactiveFormsModule],
      providers: [
        ProductService,
        UserService,
        SatisfactionService,
        AuthService
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ProductComponent);
    component = fixture.componentInstance;
    productService = TestBed.inject(ProductService);
    userService = TestBed.inject(UserService);
    satisfactionService = TestBed.inject(SatisfactionService);
    authService = TestBed.inject(AuthService);

    spyOn(productService, 'getProductByUri').and.returnValue(of(mockProduct));
    spyOn(satisfactionService, 'getProductSatisfactions').and.returnValue(of(mockReviews));
    spyOn(satisfactionService, 'createSatisfaction').and.returnValue(of({}));

    fixture.detectChanges();
  });

  it('doit créer', () => {
    expect(component).toBeTruthy();
  });

  it('doit afficher le nom du produit', () => {
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('h1')?.textContent).toContain('Test Product');
  });

  it('dois afficher la notation des avis moyens', () => {
    component.reviewsList = mockReviews;
    fixture.detectChanges();
    const compiled = fixture.nativeElement as HTMLElement;
    expect(compiled.querySelector('.text-yellow-500')?.textContent).toContain('★★★★☆');
  });

  it('dois appeller le méthode addToCart sur le button cliqué', () => {
    spyOn(component, 'addToCart');
    const button = fixture.debugElement.nativeElement.querySelector('button');
    button.click();
    expect(component.addToCart).toHaveBeenCalled();
  });

});
