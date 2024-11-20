import { Component } from '@angular/core';
import { WishlistComponent } from './component/wishlist/wishlist.component';

@Component({
  selector: 'app-wishlist-page',
  standalone: true,
  imports: [WishlistComponent],
  templateUrl: './wishlist-page.component.html',
  styleUrl: './wishlist-page.component.css'
})
export class WishlistPageComponent {

}
