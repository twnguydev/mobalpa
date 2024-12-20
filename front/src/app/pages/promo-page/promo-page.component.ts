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
  selector: 'app-promo-page',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './promo-page.component.html',
  styleUrl: './promo-page.component.css'
})
export class PromoPageComponent implements OnInit {
  allProducts: IProduct[] = [];

  products: IProduct[] = [];
  error: string | null = null;
  productAdded: { [key: string]: boolean } = {};
  isProductsLoading = false;
  isUserAuthenticated: boolean = false;
  productAddedOnCart: { [key: string]: boolean } = {};
  selectedProductColor: { [key: string]: IColor } = {};
  itemsPerPage: number = 10;
  currentPage: number = 1;
  filteredProducts: IProduct[] = [];
  selectedSort: string = '';
  selectedPrice: number = 0;
  minPrice: number = 0;
  maxPrice: number = 0;
  selectedBrand: string | null = null;
  selectedColor: string | null = null;
  colors: string[] = [];
  brands: string[] = [];
  oldPrice: number =0;
  newPrice: number = 0;

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
    this.loadProducts();

  }

  loadProducts(): void {
    this.isProductsLoading = true;
    this.productService.getProducts().subscribe(
      (products: IProduct[]) => {
        // this.products = products.filter(product => {
        //   const discountRate = product.discountRate ?? 0;
        //   const newPrice = product.newPrice ?? product.price;
        //   const oldPrice = product.oldPrice ?? product.price;
        //   return discountRate > 0 || (newPrice < oldPrice);
        // });

        // this.filteredProducts = [...this.products];
        // this.paginateProducts();

        this.allProducts = products.filter(product => {
          const campaign = product.campaigns.find(campaign => campaign.type === 'SUBCATEGORY' || campaign.type === 'PRODUCT');
          return campaign !== undefined;
        });

        this.filteredProducts = [...this.allProducts];
        this.paginateProducts();

        console.log('Products loaded', this.allProducts);
        this.isProductsLoading = false;
      },
      (error) => {
        this.error = 'Failed to load products';
        this.isProductsLoading = false;
        console.error('Error loading products', error);
      }
    );
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

  selectColor(product: IProduct, color: IColor) {
    console.log('Selected Product:', product);
    console.log('Selected Color:', color);
    console.log('Current Selection:', this.selectedProductColor[product.uuid]);
  
    if (!this.selectedProductColor[product.uuid]) {
      this.selectedProductColor[product.uuid] = { uuid: '', name: '' };
    }
    this.selectedProductColor[product.uuid] = color;
  }

  getColorHex(colorName: string): string {
    return this.colorMap[colorName] || '#CCCCCC';
  }

  paginateProducts(): void {
    const startIndex = (this.currentPage - 1) * this.itemsPerPage;
    const endIndex = startIndex + this.itemsPerPage;
    this.products = this.filteredProducts.slice(startIndex, endIndex);
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

  sortProducts(criteria: string): void {
    this.isProductsLoading = true;
    switch (criteria) {
      case 'price-asc':
        this.filteredProducts.sort((a, b) => a.price - b.price);
        break;
      case 'price-desc':
        this.filteredProducts.sort((a, b) => b.price - a.price);
        break;
      case 'relevance':
        this.filteredProducts = this.filteredProducts.sort((a, b) => {
          const aCampaign = a.campaigns.find(campaign => campaign.type === 'SUBCATEGORY');
          const bCampaign = b.campaigns.find(campaign => campaign.type === 'SUBCATEGORY');

          if (aCampaign && bCampaign) {
            return bCampaign.discountRate - aCampaign.discountRate;
          } else if (aCampaign) {
            return -1;
          } else if (bCampaign) {
            return 1;
          } else {
            return 0;
          }
        });
        break;
      default:
        break;
    }
    this.paginateProducts();
    this.isProductsLoading = false;
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

  onSortChange(event: Event): void {
    const value = (event.target as HTMLSelectElement).value;
    this.selectedSort = value;
    this.updateUrlParams();
    this.sortProducts(value);
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
}
