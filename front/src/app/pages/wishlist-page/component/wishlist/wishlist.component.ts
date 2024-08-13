import { Component } from '@angular/core';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [],
  templateUrl: './wishlist.component.html',
  styleUrl: './wishlist.component.css'
})
export class WishlistComponent {
  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    if (!this.authService.user) return console.error('User not logged in');
    this.userService.getWishlist(this.authService.user.uuid).subscribe({
      next: (wishlist) => {
        console.log('Wishlist loaded', wishlist);
      },
      error: (err) => {
        console.error('Failed to load wishlist', err);
      }
    });
  }
}
