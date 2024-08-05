import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { LoginComponent } from '../app/components/login-components/login.component'
import { HomeComponent } from '../app/components/home-components/home.component'
import { MainHeaderComponent } from '../app/components/main-header/main-header.component'
import { FooterComponent } from '../app/components/footer/footer.component'
@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    RouterLink,
    CommonModule,
    LoginComponent,
    HomeComponent,
    MainHeaderComponent,
    FooterComponent
  ]
})
export class AppComponent {
  title = 'Hello World from Angular!';

}
