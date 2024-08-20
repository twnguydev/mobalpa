import { Component, OnInit } from '@angular/core';
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
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit {
  tabs = [
    { title: 'Tout sur le produit' },
    { title: 'Caractéristiques' },
    { title: 'Avis' },
    { title: 'Caractéristiques' }
  ];

  selectedTab: number = 0;
  quantity: number = 1;
  product: IProduct | null = null;
  errorMessage: string = '';

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService
  ) {}

  ngOnInit(): void {
    const categoryUri: string | null = this.route.snapshot.paramMap.get('categoryUri');
    const subcategoryUri: string | null = this.route.snapshot.paramMap.get('subcategoryUri');
    const productUri: string | null = this.route.snapshot.paramMap.get('productUri');

    if (categoryUri && subcategoryUri && productUri) {
      this.productService.getProductByUri(categoryUri, subcategoryUri, productUri)
        .subscribe(
          (product: IProduct | null) => {
            if (product) {
              this.product = product;
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

  selectTab(index: number) {
    this.selectedTab = index;
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