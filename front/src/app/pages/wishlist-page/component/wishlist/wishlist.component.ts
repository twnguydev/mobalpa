import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { UserService } from '@services/user.service';
import { AuthService } from '@services/auth.service';
import { IWishlist } from '@interfaces/wishlist.interface';

@Component({
  selector: 'app-wishlist',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css']
})
export class WishlistComponent {
  wishlist: IWishlist | null = null;
  wishlistNotFound: boolean = false;

  constructor(private userService: UserService, private authService: AuthService) {}

  ngOnInit(): void {
    this.loadWishlist();
  }

  loadWishlist(): void {
    if (!this.authService.user) {
      console.error('User not logged in');
      return;
    }

    this.userService.getWishlist(this.authService.user.uuid).subscribe({
      next: (wishlist) => {
        console.log('Wishlist loaded', wishlist);
        this.wishlist = wishlist;
        this.wishlistNotFound = false;
      },
      error: (err) => {
        if (err.status === 404) {
          console.warn('Wishlist not found');
          this.wishlistNotFound = true;
        } else {
          console.error('Failed to load wishlist', err);
        }
      }
    });
  }
}