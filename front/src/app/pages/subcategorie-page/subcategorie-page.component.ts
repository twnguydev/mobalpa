import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '@services/product.service';
import { ISubcategory } from '@interfaces/category.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-subcategorie-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './subcategorie-page.component.html',
  styleUrls: ['./subcategorie-page.component.css']
})
export class SubcategoriePageComponent implements OnInit {
  subcategories: ISubcategory[] = [];
  categoryUri: string | null = null;
  parentCategoryName: string = '';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    this.categoryUri = this.route.snapshot.paramMap.get('categoryUri');
    if (this.categoryUri) {
      this.loadSubcategoriesByCategory(this.categoryUri);
    }
  }

  loadSubcategoriesByCategory(categoryUri: string): void {
    this.productService.getSubcategoriesByCategory(categoryUri).subscribe({
      next: (response) => {
        console.log("Subcategories loaded", response);
        this.parentCategoryName = response.parentCategoryName;
        this.subcategories = response.subcategories;
      },
      error: (error) => {
        console.error('Failed to load subcategories', error);
      }
    });
  }
}