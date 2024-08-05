import { Component } from '@angular/core';
import { BigCarouselComponent } from '../component/big-carousel/big-carousel.component';
import { SeparatorComponent } from '../component/separator/separator.component';
import { PromoComponent } from '../component/promo/promo.component';
import { CarouselMediumArticleComponent } from '../component/carousel-medium-article/carousel-medium-article.component';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [BigCarouselComponent,SeparatorComponent,PromoComponent,CarouselMediumArticleComponent],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

}
