import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink, RouterModule } from '@angular/router';
import { LoginComponent } from '../app/components/login-components/login.component';
import { HomeComponent } from '../app/components/home-components/home.component';
import { MainHeaderComponent } from './pages/main-header/main-header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';

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
    CartPageComponent
  ]
})
export class AppComponent {
  title = 'Hello World from Angular!';

}
