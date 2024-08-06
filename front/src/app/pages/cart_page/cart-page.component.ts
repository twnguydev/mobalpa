import { Component } from '@angular/core';
import { CartComponent } from './cart/component/cart.component';
import { SideComponent } from './cart/side/side.component';

@Component({
  selector: 'app-cart-page',
  standalone: true,
  imports: [CartComponent,SideComponent],
  templateUrl: './cart-page.component.html',
  styleUrl: './cart-page.component.css'
})
export class CartPageComponent {

}
