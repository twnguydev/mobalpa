import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ArticleMedium } from '../article-medium/article-medium.component';

@Component({
  selector: 'app-carousel-medium-article',
  standalone: true,
  imports: [ArticleMedium, CommonModule],
  templateUrl: './carousel-medium-article.component.html',
  styleUrl: './carousel-medium-article.component.css'
})
export class CarouselMediumArticleComponent {
  articles = [
    { title: 'Article 1', description: 'Description de l\'article 1' },
    { title: 'Article 2', description: 'Description de l\'article 2' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
    { title: 'Article 3', description: 'Description de l\'article 3' },
  ];

  currentIndex = 0;
  itemsPerPage = 4;

  get transformStyle() {
    return `translateX(-${this.currentIndex * 100}%)`;
  }

  next() {
    if (this.currentIndex < Math.floor(this.articles.length / this.itemsPerPage)) {
      this.currentIndex++;
    }
  }

  previous() {
    if (this.currentIndex > 0) {
      this.currentIndex--;
    }
  }
}