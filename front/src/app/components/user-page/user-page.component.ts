import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AdminService } from '@services/admin.service';
import { ActivatedRoute } from '@angular/router';
import { IUser } from '@interfaces/user.interface';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-page',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './user-page.component.html',
  styleUrls: ['./user-page.component.css']
})
export class UserPageComponent {
  user: IUser | null = null;

  constructor(private adminService: AdminService, private route: ActivatedRoute, private router: Router ) {}

  ngOnInit(): void {
    const uuid = this.route.snapshot.paramMap.get('uuid');
    if (uuid) {
      this.adminService.getUserByUuid(uuid).subscribe({
        next: (data) => this.user = data,
        error: (err) => console.error('Error loading user details', err)
      });
    }
  }
  goBackToAdminPanel(): void {
    this.router.navigate(['/admin']);
  }
}
