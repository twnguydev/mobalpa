import { Component, OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ProductService } from '@services/product.service';
import { UserService } from '@services/user.service';
import { IProduct, ICampaign } from '@interfaces/product.interface';
import { IWishlistItem } from '@interfaces/wishlist.interface';
import { IColor } from '@interfaces/product.interface';
import { Observable } from 'rxjs';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

interface Avis {
  rating: number;
  comment: string;
  date: string;
}

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule],
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
  ratings: number[] = [1, 2, 3, 4, 5];
  feedback = {
    rating: 0,
    comment: ''
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
  selectedColor: IColor = {} as IColor;
  
  // selectedColor: string | null = null;
  avisForm!: FormGroup;
  submissionSuccess = false;
  avisList: Avis[] = []

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private userService: UserService,
    private datePipe: DatePipe,
    private fb: FormBuilder
  ) { }

  ngOnInit(): void {
    const categoryUri: string | null = this.route.snapshot.paramMap.get('categoryUri');
    const subcategoryUri: string | null = this.route.snapshot.paramMap.get('subcategoryUri');
    const productUri: string | null = this.route.snapshot.paramMap.get('productUri');
    console.log("jhgf :" + this.product)


    this.avisForm = this.fb.group({
      rating: ['', Validators.required],
      comment: ['', [Validators.required]]
    });


    if (categoryUri && subcategoryUri && productUri) {
      this.productService.getProductByUri(categoryUri, subcategoryUri, productUri).subscribe(
        (product: IProduct | null) => {
          if (product) {
            this.product = product;
            this.selectedImage = product.images[0]?.uri || null;
            this.selectedColor = product.colors[0] || {} as IColor;
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

  selectColor(color: IColor): void {
    this.selectedColor = color;
  }
  onavisSubmit() {
    if (this.avisForm.valid) {
      const newAvis: Avis = {
        rating: this.avisForm.value.rating,
        comment: this.avisForm.value.comment,
        date: new Date().toLocaleDateString(),
      };

      this.avisList.push(newAvis); 
      this.submissionSuccess = true;
      this.avisForm.reset();

      setTimeout(() => {
        this.submissionSuccess = false;
      }, 3000);
    }
  }
  getStars(rating: number): any[] {
    return new Array(rating);
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
      const retrieveImage = this.product.images.find(image => image.color.name === this.selectedColor.name);
      const item: IWishlistItem = {
        productUuid: this.product.uuid,
        product: this.product,
        selectedColor: this.selectedColor.name,
        quantity: this.quantity,
        properties: {
          brand: this.product.brand.name,
          images: retrieveImage ? retrieveImage.uri : ''
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
