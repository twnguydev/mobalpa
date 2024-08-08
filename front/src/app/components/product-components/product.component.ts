import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product',
  standalone: true,
  imports: [CommonModule, FormsModule ],
  templateUrl: './product.component.html',
  styleUrl: './product.component.css'
})
export class ProductComponent {
  // form: FormGroup
  tabs = [
    { title: 'Tout sur le produit' },
    { title: 'Caractéristiques' },
    { title: 'Avis' },
    { title: 'Caractéristiques' }
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
  }
}
