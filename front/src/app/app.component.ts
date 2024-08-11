import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { MainHeaderComponent } from './pages/main-header/main-header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
    CommonModule,
    MainHeaderComponent,
    LandingPageComponent,
    FooterComponent,
    CartPageComponent
  ],
  templateUrl: './app.component.html',
})
export class AppComponent {
  title = 'Hello World from Angular!';

}
