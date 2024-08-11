import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './profile.component.html',
  styleUrl: './profile.component.css'
})
export class ProfileComponent {
  tabs = [
    { title: 'Données personnelles' },
    { title: 'Modes de paiement' },
    { title: 'Adresses de livraison' },
    { title: 'Commandes' }
  ];

  selectedTab: number = 0;

  selectTab(index: number) {
    this.selectedTab = index;
  }


  onSubmit() {

  }
}
