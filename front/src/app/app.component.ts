import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MainHeaderComponent } from './pages/main-header/main-header.component';
import { FooterComponent } from './pages/footer/footer.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, MainHeaderComponent, LandingPageComponent, FooterComponent, CartPageComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent {
  title = 'front';
}
