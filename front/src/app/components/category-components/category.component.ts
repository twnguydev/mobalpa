import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '@services/product.service';
import { IProduct } from '@interfaces/product.interface';
import { ISubcategory } from '@interfaces/category.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { AuthService } from '@services/auth.service';
import { UserService } from '@services/user.service';

@Component({
  selector: 'app-subcategorie-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
  allProducts: IProduct[] = [];
  filteredProducts: IProduct[] = [];

  brands: string[] = [];
  colors: string[] = [];
  minPrice: number = 0;
  maxPrice: number = 0;

  selectedBrand: string | null = null;
  selectedColor: string | null = null;
  selectedPrice: number = 0;

  categoryUri: string | null = null;
  subcategoryUri: string | null = null;
  subcategory: ISubcategory | null = null;
  productAdded: { [key: string]: boolean } = {};

  colorMap: { [key: string]: string } = {
    Rouge: '#FF0000',
    Bleu: '#0000FF',
    Noir: '#000000',
    Blanc: '#FFFFFF',
    'Jaune': '#FFFF19',
    'Gris anthracite': '#2F4F4F',
    'Bleu ciel': '#77B5FE',
    'Vert': '#3CB371',
    Violet: '#8A2BE2'
  };

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private productService: ProductService,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.categoryUri = this.route.snapshot.paramMap.get('categoryUri');
    this.subcategoryUri = this.route.snapshot.paramMap.get('subcategoryUri');
    
    this.route.queryParams.subscribe(params => {
      this.selectedBrand = params['brandName'] || null;
      this.selectedColor = params['color'] || null;
      this.selectedPrice = params['maxPrice'] ? +params['maxPrice'] : this.maxPrice;
    });
  
    if (this.subcategoryUri && this.categoryUri) {
      this.loadSubcategoryProducts(this.categoryUri, this.subcategoryUri);
      this.loadSubcategoryDetails(this.categoryUri, this.subcategoryUri);
    }
  }

  updateSelectorsFromUrl(): void {
    setTimeout(() => {
      const brandSelect = document.querySelector('select[name="brandName"]') as HTMLSelectElement;
      if (brandSelect && this.selectedBrand) {
        brandSelect.value = this.selectedBrand;
      }
  
      const colorSelect = document.querySelector('select[name="color"]') as HTMLSelectElement;
      if (colorSelect && this.selectedColor) {
        colorSelect.value = this.selectedColor;
      }
  
      const priceInput = document.querySelector('input[name="maxPrice"]') as HTMLInputElement;
      if (priceInput && this.selectedPrice) {
        priceInput.value = this.selectedPrice.toString();
      }
    });
  }

  loadSubcategoryProducts(categoryUri: string, subcategoryUri: string): void {
    this.productService.getProductsBySubcategoryUri(categoryUri, subcategoryUri).subscribe({
      next: (response) => {
        if (response && response.length > 0) {
          this.allProducts = response.flat();
          this.extractFilterData();
          this.updateSelectorsFromUrl();
          this.applyFilters();
        }
      },
      error: (error) => {
        console.error('Failed to load products', error);
      }
    });
  }

  loadSubcategoryDetails(categoryUri: string, subcategoryUri: string): void {
    this.productService.getSubcategoryByUri(categoryUri, subcategoryUri).subscribe({
      next: (subcategory) => {
        if (!subcategory) {
          return;
        }
        this.subcategory = subcategory;
      },
      error: (error) => {
        console.error('Failed to load subcategory details', error);
      }
    });
  }

  extractFilterData(): void {
    const brandSet = new Set<string>();
    const colorSet = new Set<string>();
    let minPrice = Number.MAX_VALUE;
    let maxPrice = Number.MIN_VALUE;

    this.allProducts.forEach(product => {
      brandSet.add(product.brand.name);
      product.colors.forEach(color => colorSet.add(color.name));
      if (product.price < minPrice) minPrice = product.price;
      if (product.price > maxPrice) maxPrice = product.price;
    });

    this.brands = Array.from(brandSet).sort();
    this.colors = Array.from(colorSet).sort();
    this.minPrice = minPrice === Number.MAX_VALUE ? 0 : minPrice;
    this.maxPrice = maxPrice === Number.MIN_VALUE ? 0 : maxPrice;
    this.selectedPrice = this.maxPrice;
  }

  updateUrlParams(): void {
    const queryParams: any = {};

    if (this.selectedBrand) {
        queryParams['brandName'] = this.selectedBrand;
    }

    if (this.selectedColor) {
        queryParams['color'] = this.selectedColor;
    }

    if (this.selectedPrice && this.selectedPrice !== this.maxPrice) {
        queryParams['maxPrice'] = this.selectedPrice;
    }

    const sortSelect = document.querySelector('select') as HTMLSelectElement;
    if (sortSelect && sortSelect.value) {
        queryParams['sort'] = sortSelect.value;
    }

    this.router.navigate([], {
        relativeTo: this.route,
        queryParams: queryParams,
    });

    if (this.categoryUri && this.subcategoryUri) {
        this.applyFilters();
    }
  }

  applyFilters(): void {
    this.filteredProducts = this.allProducts.filter(product => {
        return (
            (this.selectedBrand ? product.brand.name === this.selectedBrand : true) &&
            (this.selectedColor ? product.colors.some(color => color.name === this.selectedColor) : true) &&
            (product.price <= this.selectedPrice)
        );
    });
    this.sortProducts((document.querySelector('select') as HTMLSelectElement).value);
  }

  onBrandChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedBrand = value || null;
    this.updateUrlParams();
  }
  
  onColorChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedColor = value || null;
    this.updateUrlParams();
  }

  updateSelectedPrice(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const numericValue = Number(inputElement.value);
    if (!isNaN(numericValue)) {
      this.selectedPrice = numericValue;
      this.updateUrlParams();
    }
  }

  getColorHex(colorName: string): string {
    return this.colorMap[colorName] || '#CCCCCC';
  }

  addToWishlist(product: IProduct): void {
    if (!this.authService.isAuthenticated()) {
      this.authService.redirectToLogin();
      return;
    }
    this.userService.modifyWishlist(this.authService.user?.uuid ?? '', 'add', {
      productUuid: product.uuid,
      selectedColor: product.colors[0].name,
      quantity: 1
    }).subscribe({
      next: () => {
        this.productAdded[product.uuid] = true;
      },
      error: (error) => {
        console.error('Failed to add product to wishlist', error);
      }
    });
  }

  sortProducts(criteria: string): void {
    switch (criteria) {
        case 'price-asc':
            this.filteredProducts.sort((a, b) => a.price - b.price);
            break;
        case 'price-desc':
            this.filteredProducts.sort((a, b) => b.price - a.price);
            break;
        default:
            break;
    }
  }

  onSortChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.sortProducts(value);
  }
}
