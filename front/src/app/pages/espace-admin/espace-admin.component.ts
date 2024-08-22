import { Component, OnInit } from '@angular/core';
import { AdminService } from '@services/admin.service';
import { IUser } from '@interfaces/user.interface';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-espace-admin',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './espace-admin.component.html',
  styleUrls: ['./espace-admin.component.css']
})
export class EspaceAdminComponent implements OnInit {
  tabs = [
    { title: 'Utilisateur' },
    { title: 'Produits' },
    { title: 'Catégorie' },
    { title: 'Sous-Catégorie' },
    { title: 'Commande' },
    { title: 'Code Promo' },
    { title: 'Statistique' },
  ];
  selectedTab: number = 0;
  users: IUser[] = [];
  products: IProduct[] = [];
  categories: ICategory[] = [];
  subcategories: ISubcategory[] = [];
  filteredUsers: IUser[] = [];
  searchTerm: string = '';

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  selectTab(index: number) {
    this.selectedTab = index;
    if (index === 0) {
      this.loadUsers();
    } else if (index === 1) {
      this.loadProducts();
    } else if (index === 2) {
      this.loadCategories();
    }
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe({
      next: (data: IUser[]) => {
        this.users = data;
        this.filterUsers();
      },
      error: (err) => {
        console.error('Failed to load users', err);
      }
    });
  }

  loadProducts(): void {
    this.adminService.getAllProducts().subscribe({
      next: (data: IProduct[]) => {
        this.products = data;
      },
      error: (err) => {
        console.error('Failed to load products', err);
      }
    });
  }

  loadCategories(): void {
    this.adminService.getAllCategories().subscribe({
      next: (data: ICategory[]) => {
        this.categories = data;
      },
      error: (err) => {
        console.error('Failed to load categories', err);
      }
    });
  }

  loadSubcategories(): void {
    this.adminService.getAllSubcategories().subscribe({
      next: (data: ISubcategory[]) => {
        this.subcategories = data;
      },
      error: (err) => {
        console.error('Failed to load subcategories', err);
      }
    });
  }

  filterUsers(): void {
    const lowerTerm = this.searchTerm.toLowerCase();
    this.filteredUsers = this.users.filter(user =>
      user.firstname.toLowerCase().includes(lowerTerm) ||
      user.lastname.toLowerCase().includes(lowerTerm) ||
      user.email.toLowerCase().includes(lowerTerm) ||
      user.phoneNumber.toLowerCase().includes(lowerTerm) ||
      user.address?.toLowerCase().includes(lowerTerm) ||
      user.city?.toLowerCase().includes(lowerTerm) ||
      user.zipcode?.toLowerCase().includes(lowerTerm)
    );
  }
}
