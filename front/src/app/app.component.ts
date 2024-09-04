import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterModule } from '@angular/router';
import { LoginComponent } from '../app/components/login-components/login.component';
import { HomeComponent } from '../app/components/home-components/home.component';
import { MainHeaderComponent } from './pages/main-header/main-header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';
import { SubcategoriePageComponent } from './pages/subcategorie-page/subcategorie-page.component';
import { ProductComponent } from '@components/product-components/product.component';
import { ProfileComponent } from '@components/profile-components/profile.component';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    RouterModule,
    RouterLink,
    CommonModule,
    LoginComponent,
    HomeComponent,
    MainHeaderComponent,
    LandingPageComponent,
    FooterComponent,
    CartPageComponent,
    ProductComponent,
    ProfileComponent,
    SubcategoriePageComponent

  ]
})
export class AppComponent {
  title = 'Mobalpa & Archidéco - Spécialiste de la cuisine Cuisine et l\'Éctroménager';

}
