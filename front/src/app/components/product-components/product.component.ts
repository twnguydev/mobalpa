import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '@services/product.service';
import { UserService } from '@services/user.service';
import { IProduct, ICampaign } from '@interfaces/product.interface';
import { IWishlistItem } from '@interfaces/wishlist.interface';
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
  discountedPrice: number | null = null;
  discountRate: number | null = null;
  shippingDelay: string | null = null;
  isAdded: boolean = false;
  errorMessage: string = '';
  selectedImage: string | null = null;
  selectedColor: string | null = null;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private userService: UserService,
    private datePipe: DatePipe
  ) { }

  ngOnInit(): void {
    const categoryUri: string | null = this.route.snapshot.paramMap.get('categoryUri');
    const subcategoryUri: string | null = this.route.snapshot.paramMap.get('subcategoryUri');
    const productUri: string | null = this.route.snapshot.paramMap.get('productUri');
    console.log("jhgf :" + this.product)

    if (categoryUri && subcategoryUri && productUri) {
      this.productService.getProductByUri(categoryUri, subcategoryUri, productUri).subscribe(
        (product: IProduct | null) => {
          if (product) {
            this.product = product;
            this.selectedImage = product.images[0]?.uri || null;
            this.selectedColor = this.product.colors[0]?.name || null;
            this.calculateShippingDelay(product.estimatedDelivery);
            this.applyCampaigns(product.campaigns);
            console.log('Produit récupéré', product);
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

  selectImage(imageUri: string): void {
    this.selectedImage = imageUri;
  }

  selectColor(colorName: string) {
    this.selectedColor = colorName;
  }

  private applyCampaigns(campaigns: ICampaign[]): void {
    if (campaigns.length > 0) {
      const maxDiscount = Math.max(...campaigns.map(campaign => campaign.discountRate));
      const campaign = campaigns.find(c => c.discountRate === maxDiscount);

      if (campaign) {
        this.discountRate = campaign.discountRate;
        this.discountedPrice = this.product!.price * (1 - this.discountRate / 100);
      }
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

  addToCart() {
    if (this.product && this.selectedColor) {
      const item: IWishlistItem = {
        productUuid: this.product.uuid,
        product: this.product,
        selectedColor: this.selectedColor,
        quantity: this.quantity,
        properties: {
          brand: this.product.brand.name,
          images: this.product.images[0].uri
        }
      };

      this.userService.modifyCartFromLocalstorage('add', item);
      this.isAdded = true;

      setTimeout(() => {
        this.isAdded = false;
      }, 5000);
    } else {
      alert('Veuillez sélectionner une couleur avant d\'ajouter au panier.');
    }
  }

  onSubmit() {
    // Code pour gérer l'ajout au panier
  }
}
