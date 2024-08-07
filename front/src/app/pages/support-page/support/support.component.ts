import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  tabs = [
    { title: 'FAQ' },
    { title: 'Support' },
    { title: 'Votre avis' }
  ];

  selectedTab: number = 0;

  selectTab(index: number) {
    this.selectedTab = index;
  }
}
