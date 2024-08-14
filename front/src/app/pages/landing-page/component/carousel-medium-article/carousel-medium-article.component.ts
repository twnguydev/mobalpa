import { Component, HostListener } from '@angular/core';
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
  styleUrls: ['./carousel-medium-article.component.css']
})
export class CarouselMediumArticleComponent {
  products: Product[] = [
    { name: 'Produit 1', description: 'Description 1', image: 'image1.jpg', price: 10 },
    { name: 'Produit 2', description: 'Description 2', image: 'image2.jpg', price: 20 },
    { name: 'Produit 3', description: 'Description 3', image: 'image3.jpg', price: 30 },
    { name: 'Produit 4', description: 'Description 4', image: 'image4.jpg', price: 40 },
    { name: 'Produit 5', description: 'Description 5', image: 'image5.jpg', price: 50 },
    { name: 'Produit 6', description: 'Description 6', image: 'image6.jpg', price: 60 },
    { name: 'Produit 7', description: 'Description 7', image: 'image7.jpg', price: 70 },
  ];

  currentIndex = 0;
  itemsToShow = 4;

  @HostListener('window:resize')
  onResize() {
    this.updateItemsToShow();
    this.updateCarouselPosition();
  }

  ngOnInit() {
    this.updateItemsToShow();
  }

  updateItemsToShow() {
    const width = window.innerWidth;
    if (width >= 1280) { // XL
      this.itemsToShow = 4;
    } else if (width >= 1024) { // LG
      this.itemsToShow = 3;
    } else if (width >= 768) { // MD
      this.itemsToShow = 2;
    } else { // Mobile
      this.itemsToShow = 1;
    }
  }

  prev() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.updateCarouselPosition();
    }
  }

  next() {
    const maxIndex = this.products.length - this.itemsToShow;
    if (this.currentIndex < maxIndex) {
      this.currentIndex++;
      this.updateCarouselPosition();
    }
  }

  updateCarouselPosition() {
    const container = document.getElementById('carouselContainer');
    if (container) {
      const containerWidth = container.offsetWidth;
      const itemWidth = containerWidth / this.itemsToShow;
      container.scrollLeft = this.currentIndex * itemWidth;
    }
  }
}
