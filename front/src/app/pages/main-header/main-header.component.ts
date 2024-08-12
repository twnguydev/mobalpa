import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '@services/product.service';
import { ICategory } from '@interfaces/category.interface';

@Component({
  selector: 'app-main-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './main-header.component.html',
  styleUrls: ['./main-header.component.css'],
})
export class MainHeaderComponent implements OnInit {
  showSearchBar = false;
  menuOpen = false;
  categories: ICategory[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.loadCategories();
  }

  loadCategories(): void {
    this.productService.getCategories().subscribe({
      next: (categories) => {
        console.log('Categories loaded', categories);
        this.categories = categories;
      },
      error: (err) => {
        console.error('Failed to load categories', err);
      }
    });
  }

  toggleMenu() {
    this.menuOpen = !this.menuOpen;
    const menu = document.getElementById('menu');
    if (menu) {
      menu.classList.toggle('hidden', !this.menuOpen);
    }
  }

  toggleSousMenu() {
    this.menuOpen = !this.menuOpen;
    const menu = document.getElementById('sous-menu');
    if (menu) {
      menu.classList.toggle('hidden', !this.menuOpen);
    }
  }

  toggleSearchBar(): void {
    this.showSearchBar = !this.showSearchBar;
  }
}