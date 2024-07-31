import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArticleMedium } from '../article-medium/article-medium.component';
interface Product {
  name: string;
  description: string;
  image: string;
  price: number;
}
@Component({
  selector: 'app-carousel-medium-article',
  standalone: true,
  imports: [ArticleMedium, CommonModule],
  templateUrl: './carousel-medium-article.component.html',
  styleUrl: './carousel-medium-article.component.css'
})
export class CarouselMediumArticleComponent {
  products: Product[] = [
    { name: 'Produit 1', description: 'Description 1', image: 'image1.jpg', price: 10 },
    { name: 'Produit 2', description: 'Description 2', image: 'image2.jpg', price: 20 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    // Ajoutez d'autres produits ici
  ];
  currentIndex = 0;
  itemsToShow = 4;
  get transform() {
    return `translateX(-${this.currentIndex * (100 / this.itemsToShow)}%)`;
  }

  prev() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }

  next() {
    if (this.currentIndex < this.products.length - this.itemsToShow) {
      this.currentIndex++;
    }
  }
}