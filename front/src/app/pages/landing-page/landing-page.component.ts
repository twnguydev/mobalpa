import { Component } from '@angular/core';
import { BigCarouselComponent } from './component/big-carousel/big-carousel.component';
import { SeparatorComponent } from './component/separator/separator.component';
import { PromoComponent } from './component/promo/promo.component';
import { CarouselMediumArticleComponent } from './component/carousel-medium-article/carousel-medium-article.component';
import { ShowCategorieOnPageComponent } from './component/show-categorie-on-page/show-categorie-on-page.component';
import { BannerComponent } from './component/banner/banner.component';
import { NoticeComponent } from './component/notice/notice.component';

@Component({
  selector: 'app-landing-page',
  standalone: true,
  imports: [BigCarouselComponent, SeparatorComponent, PromoComponent, CarouselMediumArticleComponent, ShowCategorieOnPageComponent, BannerComponent,NoticeComponent],
  templateUrl: './landing-page.component.html',
  styleUrl: './landing-page.component.css'
})
export class LandingPageComponent {

}
