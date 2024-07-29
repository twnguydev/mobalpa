import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { RouterOutlet, RouterLink } from '@angular/router';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  imports: [
    RouterOutlet,
    RouterLink,
    CommonModule
  ]
})
export class AppComponent {
  title = 'Hello World from Angular!';

  constructor(public authService: AuthService) { }

  logout() {
    this.authService.logout();
  }
}
