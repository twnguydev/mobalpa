import { Component } from '@angular/core';
import { ProductService } from '@services/product.service';
import { ICategory } from '@interfaces/category.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-show-categorie-on-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './show-categorie-on-page.component.html',
  styleUrl: './show-categorie-on-page.component.css'
})
export class ShowCategorieOnPageComponent {
  categories: ICategory[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe({
      next: (response) => {
        console.log('Categories loaded', response);
        this.categories = response;
      },
      error: (error) => {
        console.error('Failed to load categories', error);
      }
    });
  }
}
