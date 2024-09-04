import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubcategoriePageComponent } from './subcategorie-page.component';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { ProductService } from '@services/product.service';
import { of } from 'rxjs';
import { By } from '@angular/platform-browser';

describe('SubcategoriePageComponent', () => {
  let component: SubcategoriePageComponent;
  let fixture: ComponentFixture<SubcategoriePageComponent>;
  let route: ActivatedRoute;
  let productService: ProductService;

  beforeEach(async () => {
    const paramMap = new Map<string, string>();
    paramMap.set('categoryUri', 'categorie-1');

    const subcategories = [
      { uri: 'table', name: 'chaise' },
    ];

    const productServiceMock = {
      getSubcategoriesByCategoryUri: () => of({
        parentCategoryName: 'cuisines',
        subcategories,
      }),
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterModule.forRoot([]),
        SubcategoriePageComponent,
      ],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of(paramMap),
            snapshot: {
              paramMap,
            },
          },
        },
        {
          provide: ProductService,
          useValue: productServiceMock,
        },
      ],
    })
      .compileComponents();

    fixture = TestBed.createComponent(SubcategoriePageComponent);
    component = fixture.componentInstance;
    route = TestBed.inject(ActivatedRoute);
    productService = TestBed.inject(ProductService);
    fixture.detectChanges();
  });

  it('devrait afficher les sous-catégories', () => {
    const linkElements = fixture.debugElement.queryAll(By.css('.sublink'));
    expect(linkElements.length).toBeGreaterThan(0);

    linkElements.forEach((linkElement) => {
      const link = linkElement.nativeElement.getAttribute('href');
      expect(link).toContain('/categories/');
    });
  });
});