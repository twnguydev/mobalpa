import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '@services/product.service';
import { IProduct } from '@interfaces/product.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-category',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {
  products: IProduct[] = [];
  categoryUri: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.categoryUri = this.route.snapshot.paramMap.get('categoryUri');
    if (this.categoryUri) {
      this.loadProductsByCategory(this.categoryUri);
    }
  }

  loadProductsByCategory(categoryUri: string): void {
    this.productService.getProductsByCategory(categoryUri).subscribe({
      next: (products) => {
        console.log("products", products);
        this.products = products;
      },
      error: (error) => {
        console.error('Failed to load products', error);
      }
    });
  }
}