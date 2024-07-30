import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MainHeaderComponent } from './component/main-header/main-header.component';
import { BigCarouselComponent } from './component/big-carousel/big-carousel.component';
import { PromoComponent } from "./component/promo/promo.component";
import { ShowCategorieOnPageComponent } from './component/show-categorie-on-page/show-categorie-on-page.component';
import { SeparatorComponent } from './component/separator/separator.component';
import { CarouselMediumArticleComponent } from './component/carousel-medium-article/carousel-medium-article.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MainHeaderComponent, BigCarouselComponent, PromoComponent, ShowCategorieOnPageComponent, SeparatorComponent, CarouselMediumArticleComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front';
}
