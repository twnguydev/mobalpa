import { Component,Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IProduct } from '@interfaces/product.interface';
import { UserService } from '@services/user.service';

@Component({
  selector: 'app-medium-article',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './article-medium.component.html',
  styleUrl: './article-medium.component.css'
})
export class ArticleMedium {
  @Input() product!: any;
  productAddedOnCart: { [key: string]: boolean } = {};

  constructor(private userService: UserService) { }

  ngOnInit(): void {
  }

  addToCart(product: IProduct): void {
    const item = {
      productUuid: product.uuid,
      product: product,
      selectedColor: product.colors[0].name,
      quantity: 1,
      properties: {
        brand: product.brand.name,
        images: product.images[0].uri
      }
    };
    this.userService.modifyCartFromLocalstorage('add', item);
    this.productAddedOnCart[product.uuid] = true;
    setTimeout(() => {
      this.productAddedOnCart[product.uuid] = false;
    }, 5000);
  }
}
