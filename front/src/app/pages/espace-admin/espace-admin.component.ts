import { Component, OnInit } from '@angular/core';
import { AdminService } from '@services/admin.service';
import { IUser } from '@interfaces/user.interface';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-espace-admin',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './espace-admin.component.html',
  styleUrls: ['./espace-admin.component.css']
})
export class EspaceAdminComponent implements OnInit {
  tabs = [
    { title: 'Utilisateur' },
    { title: 'Produits' },
    { title: 'Catégorie' },
    { title: 'Sous-Catégorie' },
  ];
  selectedTab: number = 0;
  users: IUser[] = [];

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  selectTab(index: number) {
    this.selectedTab = index;
    if (index === 0) {
      this.loadUsers();
    }
  }

  loadUsers(): void {
    this.adminService.getAll().subscribe({
      next: (data: IUser[]) => {
        this.users = data;
      },
      error: (err) => {
        console.error('Failed to load users', err);
      }
    });
  }

}
