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
    this.productService.getProducts().subscribe(
      (products: IProduct[]) => {
        const currentDate = new Date();

        const startOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth(), 1);

        const endOfMonth = new Date(currentDate.getFullYear(), currentDate.getMonth() + 1, 0);

        this.products = products.filter(product => {
          if (!product.createdAt) {
            return false;
          }
          const productDate = new Date(product.createdAt);
          return productDate >= startOfMonth && productDate <= endOfMonth;
        });

        this.products.sort((a, b) => {
          const dateA = a.createdAt ? new Date(a.createdAt).getTime() : 0;
          const dateB = b.createdAt ? new Date(b.createdAt).getTime() : 0;
          return dateB - dateA;
        });

        this.filteredProducts = [...this.products];

        this.paginateProducts();
      },
      (error) => {
        this.error = 'Failed to load products';
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
  selectColor(product: IProduct, color: IColor): void {
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
