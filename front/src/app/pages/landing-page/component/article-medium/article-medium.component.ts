import { Component,Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { IProduct } from '@interfaces/product.interface';

@Component({
  selector: 'app-medium-article',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './article-medium.component.html',
  styleUrl: './article-medium.component.css'
})
export class ArticleMedium {
  @Input() product!: any;
}
