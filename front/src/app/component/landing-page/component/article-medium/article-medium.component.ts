import { Component,Input } from '@angular/core';

@Component({
  selector: 'app-medium-article',
  standalone: true,
  imports: [],
  templateUrl: './article-medium.component.html',
  styleUrl: './article-medium.component.css'
})
export class ArticleMedium {
  @Input() data: any;
}
