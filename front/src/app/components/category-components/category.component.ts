import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '@services/product.service';
import { IProduct } from '@interfaces/product.interface';
import { ISubcategory } from '@interfaces/category.interface';
import { CommonModule } from '@angular/common';
import { AuthService } from '@services/auth.service';
import { UserService } from '@services/user.service';

@Component({
  selector: 'app-subcategorie-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
  products: IProduct[] = [];
  categoryUri: string | null = null;
  subcategoryUri: string | null = null;
  subcategory: ISubcategory | null = null;

  colorMap: { [key: string]: string } = {
    Rouge: '#FF0000',
    Bleu: '#0000FF',
    Noir: '#000000',
    Blanc: '#FFFFFF',
    'Gris anthracite': '#2F4F4F',
    Violet: '#8A2BE2'
  };  

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private authService: AuthService,
    private userService: UserService
  ) { }

  ngOnInit(): void {
    this.categoryUri = this.route.snapshot.paramMap.get('categoryUri');
    this.subcategoryUri = this.route.snapshot.paramMap.get('subcategoryUri');
    if (this.subcategoryUri && this.categoryUri) {
      console.log(this.categoryUri);
      console.log(this.subcategoryUri);
      this.loadSubcategoryProducts(this.categoryUri, this.subcategoryUri);
      this.loadSubcategoryDetails(this.categoryUri, this.subcategoryUri);
    }
  }

  loadSubcategoryProducts(categoryUri: string, subcategoryUri: string): void {
    this.productService.getProductsBySubcategoryUri(categoryUri, subcategoryUri).subscribe({
      next: (response) => {
        console.log("Products loaded", response);
        if (response && response.length > 0) {
          this.products = response.flat();
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
        console.log('Subcategory details loaded', subcategory);
      },
      error: (error) => {
        console.error('Failed to load subcategory details', error);
      }
    });
  }

  getColorHex(colorName: string): string {
    return this.colorMap[colorName] || '#CCCCCC';
  }

  addToWishlist(product: IProduct): void {
    if (!this.authService.isAuthenticated()) {
      this.authService.redirectToLogin();
      return;
    }
    console.log('Adding product to wishlist', product);
    this.userService.modifyWishlist(this.authService.user?.uuid ?? '', 'add', {
      productUuid: product.uuid,
      selectedColor: product.colors[0].name,
      quantity: 1
    }).subscribe({
      next: () => {
        console.log('Product added to wishlist');
      },
      error: (error) => {
        console.error('Failed to add product to wishlist', error);
      }
    })
  }
}
