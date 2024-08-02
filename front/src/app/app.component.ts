import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { LoginComponent } from '../app/components/login-components/login.component'
import { HomeComponent } from '../app/components/home-components/home.component'

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    RouterLink,
    CommonModule,
    LoginComponent,
    HomeComponent
  ]
})
export class AppComponent {
  title = 'Hello World from Angular!';

}
