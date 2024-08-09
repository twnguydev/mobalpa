import { Component } from '@angular/core';
import { SideComponent } from '../side/side.component';

@Component({
  selector: 'app-cart',
  standalone: true,
  imports: [SideComponent],
  templateUrl: './cart.component.html',
  styleUrl: './cart.component.css'
})
export class CartComponent {

}
