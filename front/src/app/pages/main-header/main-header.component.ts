import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';



@Component({
  selector: 'app-main-header',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './main-header.component.html',
  styleUrls: ['./main-header.component.css'],
  
})
export class MainHeaderComponent {
  showSearchBar = false;
  menuOpen = false;
  toggleMenu() {
    this.menuOpen = !this.menuOpen;
    const menu = document.getElementById('menu');
    if (menu) {
      menu.classList.toggle('hidden', !this.menuOpen);
    }
  }


  toggleSousMenu() {
    this.menuOpen = !this.menuOpen;
    const menu = document.getElementById('sous-menu');
    if (menu) {
      menu.classList.toggle('hidden', !this.menuOpen);
    }
  }
  toggleSearchBar(): void {
    this.showSearchBar = !this.showSearchBar;
  }


  
}