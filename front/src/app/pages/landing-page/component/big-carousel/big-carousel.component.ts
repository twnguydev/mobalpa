import { Component } from '@angular/core';

@Component({
  selector: 'app-big-carousel',
  standalone: true,
  imports: [],
  templateUrl: './big-carousel.component.html',
  styleUrl: './big-carousel.component.css'
})
export class BigCarouselComponent {
  currentIndex = 0;
  intervalId: any; 

  showSlide(index: number) {
    const slides = document.querySelectorAll('.carousel-slide');
    const totalSlides = slides.length;
    this.currentIndex = (index + totalSlides) % totalSlides;

    const carousel = document.getElementById('carouselSlides');
    if (carousel) {
      carousel.style.transform = `translateX(-${this.currentIndex * 100}%)`;
    }
  }

  nextSlide() {
    this.showSlide(this.currentIndex + 1);
  }

  prevSlide() {
    this.showSlide(this.currentIndex - 1);
  }

  ngAfterViewInit() {
    this.showSlide(this.currentIndex);

    this.intervalId = setInterval(() => this.nextSlide(), 8000); 
  }

  ngOnDestroy() {
    if (this.intervalId) {
      clearInterval(this.intervalId);
    }
  }
}
