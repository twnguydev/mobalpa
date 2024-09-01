import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductService } from '@services/product.service';
import { IProduct, IColor } from '@interfaces/product.interface';
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

  itemsPerPage: number = 6;
  currentPage: number = 1;
  paginatedProducts: IProduct[] = [];

  brands: string[] = [];
  colors: string[] = [];
  minPrice: number = 0;
  maxPrice: number = 0;

  selectedBrand: string | null = null;
  selectedColor: string | null = null;
  selectedPrice: number = 0;
  selectedSort: string = '';

  categoryUri: string | null = null;
  subcategoryUri: string | null = null;
  subcategory: ISubcategory | null = null;
  productAdded: { [key: string]: boolean } = {};
  productAddedOnCart: { [key: string]: boolean } = {};
  selectedProductColor: { [key: string]: IColor } = {};

  isUserAuthenticated: boolean = false;

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
      this.selectedSort = params['sort'] || '';

      if (this.subcategoryUri && this.categoryUri) {
        this.loadSubcategoryProducts(this.categoryUri, this.subcategoryUri);
        this.loadSubcategoryDetails(this.categoryUri, this.subcategoryUri);
      }
    });

    this.isUserAuthenticated = this.authService.isAuthenticated();
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

      const sortSelect = document.querySelector('select[name="sort"]') as HTMLSelectElement;
      if (sortSelect && this.selectedSort) {
        sortSelect.value = this.selectedSort;
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
          console.log('Products loaded', this.allProducts);
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
    this.selectedPrice = this.selectedPrice || this.maxPrice;
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

    if (this.selectedSort) {
      queryParams['sort'] = this.selectedSort;
    }

    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: queryParams,
    });
  }

  applyFilters(): void {
    this.filteredProducts = this.allProducts.filter(product => {
      return (
        (this.selectedBrand ? product.brand.name === this.selectedBrand : true) &&
        (this.selectedColor ? product.colors.some(color => color.name === this.selectedColor) : true) &&
        (product.price <= this.selectedPrice)
      );
    });

    this.sortProducts(this.selectedSort);
    this.currentPage = 1;
    this.paginateProducts();
  }

  onBrandChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedBrand = value || null;
    this.updateUrlParams();
    this.applyFilters();
  }

  onColorChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedColor = value || null;
    this.updateUrlParams();
    this.applyFilters();
  }

  updateSelectedPrice(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    const numericValue = Number(inputElement.value);
    if (!isNaN(numericValue)) {
      this.selectedPrice = numericValue;
      this.updateUrlParams();
      this.applyFilters();
    }
  }

  selectColor(product: IProduct, color: IColor) {
    if (!this.selectedProductColor[product.uuid]) {
      this.selectedProductColor[product.uuid] = { uuid: '', name: '' };
    }

    this.selectedProductColor[product.uuid] = color;
    
    console.log('Selected Product:', product);
    console.log('Selected Color:', color);
    console.log('Current Selection:', this.selectedProductColor[product.uuid]);
  }

  getColorHex(colorName: string): string {
    return this.colorMap[colorName] || '#CCCCCC';
  }

  discountedPrice(product: IProduct): number | null {
    const campaign = product.campaigns.find(campaign => campaign.type === 'SUBCATEGORY');
    if (campaign) {
      return product.price * (1 - campaign.discountRate / 100);
    }
    return null;
  }

  discountRate(product: IProduct): number | null {
    const campaign = product.campaigns.find(campaign => campaign.type === 'SUBCATEGORY');
    if (campaign) {
      return campaign.discountRate;
    }
    return null;
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

        setTimeout(() => {
          this.productAdded[product.uuid] = false;
        }, 5000);
      },
      error: (error) => {
        console.error('Failed to add product to wishlist', error);
      }
    });
  }

  addToCart(product: IProduct): void {
    if (this.selectedProductColor[product.uuid] === undefined) {
      this.selectedProductColor[product.uuid] = product.colors[0];
    }
    const retrieveImage = product.images.find(image => image.color.name === this.selectedProductColor[product.uuid].name);
    const item = {
      productUuid: product.uuid,
      product: product,
      selectedColor: this.selectedProductColor[product.uuid].name,
      quantity: 1,
      properties: {
        brand: product.brand.name,
        images: retrieveImage ? retrieveImage.uri : ''
      }
    };
    this.userService.modifyCartFromLocalstorage('add', item);
    this.productAddedOnCart[product.uuid] = true;
    setTimeout(() => {
      this.productAddedOnCart[product.uuid] = false;
    }, 5000);
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
    this.paginateProducts();
  }

  onSortChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedSort = value;
    this.updateUrlParams();
    this.sortProducts(value);
  }

  paginateProducts(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.paginatedProducts = this.filteredProducts.slice(startIndex, endIndex);
  }

  nextPage(): void {
    if (this.currentPage < this.totalPages) {
      this.currentPage++;
      this.paginateProducts();
    }
  }

  previousPage(): void {
    if (this.currentPage > 1) {
      this.currentPage--;
      this.paginateProducts();
    }
  }

  goToPage(page: number): void {
    if (page >= 1 && page <= this.totalPages) {
      this.currentPage = page;
      this.paginateProducts();
    }
  }

  get totalPages(): number {
    return Math.ceil(this.filteredProducts.length / this.itemsPerPage);
  }

  getDisplayedPages(): number[] {
    const startPage = Math.max(2, this.currentPage - 1);
    const endPage = Math.min(this.totalPages - 1, this.currentPage + 1);

    const pages = [];
    for (let i = startPage; i <= endPage; i++) {
      pages.push(i);
    }
    return pages;
  }
}