import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '@services/product.service';
import { IProduct } from '@interfaces/product.interface';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css'],
  providers: [DatePipe]
})
export class ProductComponent implements OnInit {
  tabs = [
    { title: 'Tout sur le produit' },
    { title: 'Caractéristiques' },
    { title: 'Avis' },
  ];

  colorMap: { [key: string]: string } = {
    Rouge: '#FF0000',
    Bleu: '#0000FF',
    Noir: '#000000',
    Blanc: '#FFFFFF',
    'Jaune': '#FFFF19',
    'Gris anthracite': '#2F4F4F',
    'Bleu ciel': '#77B5FE',
    'Vert': '#3CB371',
    Violet: '#8A2BE2'
  };

  selectedTab: number = 0;
  quantity: number = 1;
  product: IProduct | null = null;
  shippingDelay: string | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    const categoryUri: string | null = this.route.snapshot.paramMap.get('categoryUri');
    const subcategoryUri: string | null = this.route.snapshot.paramMap.get('subcategoryUri');
    const productUri: string | null = this.route.snapshot.paramMap.get('productUri');

    if (categoryUri && subcategoryUri && productUri) {
      this.productService.getProductByUri(categoryUri, subcategoryUri, productUri).subscribe(
        (product: IProduct | null) => {
          if (product) {
            this.product = product;
            this.calculateShippingDelay(product.estimatedDelivery);
          } else {
            this.errorMessage = 'Produit non trouvé.';
          }
        },
        error => {
          console.error('Erreur lors de la récupération du produit', error);
          this.errorMessage = 'Erreur lors de la récupération du produit.';
        }
      );
    } else {
      this.errorMessage = 'URL invalide.';
    }
  }

  private calculateShippingDelay(dateString: string): void {
    const now = new Date();
    const estimatedDeliveryDate = new Date(dateString);
    
    const totalDays = Math.ceil((estimatedDeliveryDate.getTime() - now.getTime()) / (1000 * 60 * 60 * 24));
    
    if (totalDays <= 0) {
      this.shippingDelay = 'Expédition immédiate';
      return;
    }

    const daysInMonth = 30;
    const daysInWeek = 7;
    
    const months = Math.floor(totalDays / daysInMonth);
    const weeks = Math.floor((totalDays % daysInMonth) / daysInWeek);
    const days = totalDays % daysInWeek;

    let delayParts: string[] = [];
    if (months > 0) delayParts.push(`${months} mois`);
    if (weeks > 0) delayParts.push(`${weeks} semaines`);
    if (days > 0) delayParts.push(`${days} jours`);

    this.shippingDelay = `Expédition sous ${delayParts.join(', ')}`;
  }

  selectTab(index: number) {
    this.selectedTab = index;
  }

  getColorHex(colorName: string): string {
    return this.colorMap[colorName] || '#CCCCCC';
  }

  increaseQuantity() {
    if (this.product && this.quantity < this.product.stock) {
      this.quantity++;
    }
  }

  decreaseQuantity() {
    if (this.quantity > 1) {
      this.quantity--;
    }
  }

  onSubmit() {
    // Code pour gérer l'ajout au panier
  }
}