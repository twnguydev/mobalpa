import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './support.component.html',
  styleUrl: './support.component.css'
})
export class SupportComponent {
  tabs = [
    { title: 'FAQ' },
    { title: 'Support' },
    { title: 'Votre avis' }
  ];
  ratings: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  feedback = {
    rating: 0,
    comment: ''
  };



  selectedTab: number = 0;

  selectTab(index: number) {
    this.selectedTab = index;
  }


  onSubmit() {
    console.log('feedback send');

    //liaison back end
  }
}
