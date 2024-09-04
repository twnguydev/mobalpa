import { Component, HostListener } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ArticleMedium } from '../article-medium/article-medium.component';
import { ProductService } from '@services/product.service';
import { IProduct, ICampaign } from '@interfaces/product.interface';

@Component({
  selector: 'app-carousel-medium-article',
  standalone: true,
  imports: [ArticleMedium, CommonModule],
  templateUrl: './carousel-medium-article.component.html',
  styleUrls: ['./carousel-medium-article.component.css']
})
export class CarouselMediumArticleComponent {
  products: IProduct[] = {} as IProduct[];

  currentIndex = 0;
  itemsToShow = 4;

  constructor(private productService: ProductService) { }

  @HostListener('window:resize')
  onResize() {
    this.updateItemsToShow();
    this.updateCarouselPosition();
  }

  ngOnInit() {
    this.updateItemsToShow();
    this.loadProductsWithCampaign();
  }

  loadProductsWithCampaign(): void {
    this.productService.getProductsWithCampaign().subscribe({
      next: (products) => {
        this.products = this.findPromoSoonToEnd(products);
        console.log('Promo loaded', this.products);
        console.log('Products loaded', products);
      },
      error: (err) => {
        console.error('Failed to load promo', err);
      }
    });
  }

  findPromoSoonToEnd(products: IProduct[]): IProduct[] {
    const now = new Date();
    const threeDaysFromNow = new Date();
    threeDaysFromNow.setDate(now.getDate() + 3);

    const productsWithCampaignSoonToEnd: IProduct[] = products.filter(product => {
      const campaign: ICampaign | undefined = product.campaigns.find(campaign => {
        const campaignEndDate = new Date(campaign.dateEnd);
        return campaignEndDate > now && campaignEndDate <= threeDaysFromNow;
      });
      return !!campaign;
    });

    return productsWithCampaignSoonToEnd;
  }

  updateItemsToShow(): void {
    const width: number = window.innerWidth;
    if (width >= 1280) {
      this.itemsToShow = 4;
    } else if (width >= 1024) {
      this.itemsToShow = 3;
    } else if (width >= 768) {
      this.itemsToShow = 2;
    } else {
      this.itemsToShow = 1;
    }
  }

  prev(): void {
    if (this.currentIndex > 0) {
      this.currentIndex--;
      this.updateCarouselPosition();
    }
  }

  next(): void {
    const maxIndex: number = this.products.length - this.itemsToShow;
    if (this.currentIndex < maxIndex) {
      this.currentIndex++;
      this.updateCarouselPosition();
    }
  }

  updateCarouselPosition(): void {
    const container: HTMLElement | null = document.getElementById('carouselContainer');
    if (container) {
      const containerWidth: number = container.offsetWidth;
      const itemWidth: number = containerWidth / this.itemsToShow;
      container.scrollLeft = this.currentIndex * itemWidth;
    }
  }

  testLink(): void {
    console.log('Test link');
  }
}
