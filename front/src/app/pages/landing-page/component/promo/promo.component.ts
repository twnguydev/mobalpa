import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ProductService } from '@services/product.service';

@Component({
  selector: 'app-promo',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './promo.component.html',
  styleUrls: ['./promo.component.css']
})
export class PromoComponent implements OnInit {
  products: any[] = [];

  constructor(private productService: ProductService) { }

  ngOnInit(): void {
    this.loadPromo();
  }

  loadPromo(): void {
    this.productService.getProductsWithCampaign().subscribe({
      next: (products) => {
        this.products = this.retrieveBestDiscountRates(products);
        console.log('Promo loaded', this.products);
      },
      error: (err) => {
        console.error('Failed to load promo', err);
      }
    });
  }

  retrieveBestDiscountRates(products: any[]): any[] {
    const productsWithDiscountRates = products.map(product => {
      const discountRate = product.campaigns.reduce((maxRate: number, campaign: any) => {
        return campaign.discountRate > maxRate ? campaign.discountRate : maxRate;
      }, 0);

      return {
        ...product,
        discountRate,
        originalPrice: product.product.price,
        discountedPrice: product.product.price - (product.product.price * discountRate / 100),
        imageUrl: product.product.images[0]?.uri
      };
    });

    productsWithDiscountRates.sort((a, b) => b.discountRate - a.discountRate);
    return productsWithDiscountRates.slice(0, 3);
  }
}