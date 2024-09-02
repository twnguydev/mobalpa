import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '@services/admin.service';
import { ActivatedRoute, Router } from '@angular/router';
import { IUser } from '@interfaces/user.interface';
import { IWishlist } from '@interfaces/wishlist.interface';
import { ITicket } from '@interfaces/tickets.interface';


@Component({
  selector: 'app-user-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent implements OnInit {
  user: IUser | null = null;
  wishlists: IWishlist[] = [];
  tickets: ITicket[] = [];

  constructor(
    private adminService: AdminService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.adminService.getUserByUuid(uuid).subscribe({
        next: (data) => {
          this.user = data;
          this.tickets = data.tickets ?? [];
        },
        error: (err) => console.error('Error loading user details', err)
      });
    }
  }

  goBackToAdminPanel(): void {
    this.router.navigate(['/admin']);
  }
}
