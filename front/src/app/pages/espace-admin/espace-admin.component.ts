import { Component, OnInit } from '@angular/core';
import { AdminService } from '@services/admin.service';
import { IUser } from '@interfaces/user.interface';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { IOrder } from '@interfaces/order.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '@components/pagination/pagination.component';

@Component({
  selector: 'app-espace-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
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

  // Variables pour les utilisateurs
  users: IUser[] = [];
  filteredUsers: IUser[] = [];
  searchTermUsers: string = '';
  currentPageUsers: number = 1;
  itemsPerPageUsers: number = 10;
  totalPagesUsers: number = 1;

  // Variables pour les produits
  products: IProduct[] = [];
  filteredProducts: IProduct[] = [];
  searchTermProducts: string = '';
  currentPageProducts: number = 1;
  itemsPerPageProducts: number = 10;
  totalPagesProducts: number = 1;

  // Variables pour les catégories
  categories: ICategory[] = [];
  filteredCategories: ICategory[] = [];
  searchTermCategories: string = '';
  currentPageCategories: number = 1;
  itemsPerPageCategories: number = 10;
  totalPagesCategories: number = 1;

  // Variables pour les sous-catégories
  subcategories: ISubcategory[] = [];
  filteredSubcategories: ISubcategory[] = [];
  searchTermSubcategories: string = '';
  currentPageSubcategories: number = 1;
  itemsPerPageSubcategories: number = 10;
  totalPagesSubcategories: number = 1;

  // Variables pour les commandes
  orders: IOrder[] = [];
  filteredOrders: IOrder[] = [];
  searchTermOrders: string = '';
  currentPageOrders: number = 1;
  itemsPerPageOrders: number = 10;
  totalPagesOrders: number = 1;

  // Variables pour les codes promo
  codePromos: any[] = [];
  filteredCodePromos: any[] = [];
  searchTermCodePromos: string = '';
  currentPageCodePromos: number = 1;
  itemsPerPageCodePromos: number = 10;
  totalPagesCodePromos: number = 1;

  // Variables pour les statistiques
  statistics: any[] = [];
  filteredStatistics: any[] = [];
  searchTermStatistics: string = '';
  currentPageStatistics: number = 1;
  itemsPerPageStatistics: number = 10;
  totalPagesStatistics: number = 1;

  constructor(private adminService: AdminService) { }

  ngOnInit(): void {
    this.loadUsers();
  }

  selectTab(index: number) {
    this.selectedTab = index;
    switch (index) {
      case 0: this.loadUsers(); break;
      case 1: this.loadProducts(); break;
      case 2: this.loadCategories(); break;
      case 3: this.loadSubcategories(); break;
      case 4: this.loadOrders(); break;
      case 5: this.loadCodePromos(); break;
      case 6: this.loadStatistics(); break;
    }
  }

  loadUsers(): void {
    this.adminService.getAllUsers().subscribe(users => {
      this.users = users;
      this.filterUsers();
    });
  }

  filterUsers(): void {
    const filtered = this.users.filter(user =>
      user.firstname.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.lastname.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.email.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.phoneNumber.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.address?.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.city?.toLowerCase().includes(this.searchTermUsers.toLowerCase()) ||
      user.zipcode?.toLowerCase().includes(this.searchTermUsers.toLowerCase())
    );

    this.totalPagesUsers = Math.ceil(filtered.length / this.itemsPerPageUsers);
    this.currentPageUsers = Math.min(this.currentPageUsers, this.totalPagesUsers);
    this.filteredUsers = filtered.slice((this.currentPageUsers - 1) * this.itemsPerPageUsers, this.currentPageUsers * this.itemsPerPageUsers);
  }

  onPageChangeUsers(page: number): void {
    this.currentPageUsers = page;
    this.filterUsers();
  }

  loadProducts(): void {
    this.adminService.getAllProducts().subscribe(products => {
      this.products = products;
      this.filterProducts();
    });
  }

  filterProducts(): void {
    const filtered = this.products.filter(product =>
      product.name.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.description.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.price.toString().toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.stock.toString().toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.estimatedDelivery.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.weight.toString().toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.height.toString().toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.width.toString().toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.category.name.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.subcategory.name.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.brand.name.toLowerCase().includes(this.searchTermProducts.toLowerCase()) ||
      product.colors.map(color => color.name).join(',').toLowerCase().includes(this.searchTermProducts.toLowerCase())
    );

    this.totalPagesProducts = Math.ceil(filtered.length / this.itemsPerPageProducts);
    this.currentPageProducts = Math.min(this.currentPageProducts, this.totalPagesProducts);
    this.filteredProducts = filtered.slice((this.currentPageProducts - 1) * this.itemsPerPageProducts, this.currentPageProducts * this.itemsPerPageProducts);
  }

  onPageChangeProducts(page: number): void {
    this.currentPageProducts = page;
    this.filterProducts();
  }

  loadCategories(): void {
    this.adminService.getAllCategories().subscribe(categories => {
      this.categories = categories;
      this.filterCategories();
    });
  }

  filterCategories(): void {
    const filtered = this.categories.filter(category =>
      category.name.toLowerCase().includes(this.searchTermCategories.toLowerCase()) ||
      category.description.toLowerCase().includes(this.searchTermCategories.toLowerCase())
    );

    this.totalPagesCategories = Math.ceil(filtered.length / this.itemsPerPageCategories);
    this.currentPageCategories = Math.min(this.currentPageCategories, this.totalPagesCategories);
    this.filteredCategories = filtered.slice((this.currentPageCategories - 1) * this.itemsPerPageCategories, this.currentPageCategories * this.itemsPerPageCategories);
  }

  onPageChangeCategories(page: number): void {
    this.currentPageCategories = page;
    this.filterCategories();
  }

  loadSubcategories(): void {
    this.adminService.getAllSubcategories().subscribe(subcategories => {
      this.subcategories = subcategories;
      this.filterSubcategories();
    });
  }

  filterSubcategories(): void {
    const filtered = this.subcategories.filter(subcategory =>
      subcategory.name.toLowerCase().includes(this.searchTermSubcategories.toLowerCase()) ||
      subcategory.description.toLowerCase().includes(this.searchTermSubcategories.toLowerCase())
    );

    this.totalPagesSubcategories = Math.ceil(filtered.length / this.itemsPerPageSubcategories);
    this.currentPageSubcategories = Math.min(this.currentPageSubcategories, this.totalPagesSubcategories);
    this.filteredSubcategories = filtered.slice((this.currentPageSubcategories - 1) * this.itemsPerPageSubcategories, this.currentPageSubcategories * this.itemsPerPageSubcategories);
  }

  onPageChangeSubcategories(page: number): void {
    this.currentPageSubcategories = page;
    this.filterSubcategories();
  }

  loadOrders(): void {
    this.adminService.getAllOrders().subscribe(orders => {
      this.orders = orders;
      this.filterOrders();
    });
  }

  filterOrders(): void {
    const filtered = this.orders.filter(order =>
      order.uuid?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.user?.firstname?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.user?.lastname?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.totalTtc?.toString().toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.deliveryAddress?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.status?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.createdAt?.toLowerCase().includes(this.searchTermOrders.toLowerCase())
    );

    this.totalPagesOrders = Math.ceil(filtered.length / this.itemsPerPageOrders);
    this.currentPageOrders = Math.min(this.currentPageOrders, this.totalPagesOrders);
    this.filteredOrders = filtered.slice((this.currentPageOrders - 1) * this.itemsPerPageOrders, this.currentPageOrders * this.itemsPerPageOrders);
  }

  onPageChangeOrders(page: number): void {
    this.currentPageOrders = page;
    this.filterOrders();
  }

  loadCodePromos(): void {
    // this.adminService.getAllCodePromos().subscribe(codePromos => {
    //   this.codePromos = codePromos;
    //   this.filterCodePromos();
    // });
  }

  filterCodePromos(): void {
    const filtered = this.codePromos.filter(codePromo =>
      codePromo.code.toLowerCase().includes(this.searchTermCodePromos.toLowerCase()) ||
      codePromo.description.toLowerCase().includes(this.searchTermCodePromos.toLowerCase())
    );

    this.totalPagesCodePromos = Math.ceil(filtered.length / this.itemsPerPageCodePromos);
    this.currentPageCodePromos = Math.min(this.currentPageCodePromos, this.totalPagesCodePromos);
    this.filteredCodePromos = filtered.slice((this.currentPageCodePromos - 1) * this.itemsPerPageCodePromos, this.currentPageCodePromos * this.itemsPerPageCodePromos);
  }

  onPageChangeCodePromos(page: number): void {
    this.currentPageCodePromos = page;
    this.filterCodePromos();
  }

  loadStatistics(): void {
    // this.adminService.getAllStatistics().subscribe(statistics => {
    //   this.statistics = statistics;
    //   this.filterStatistics();
    // });
  }

  filterStatistics(): void {
    const filtered = this.statistics;

    this.totalPagesStatistics = Math.ceil(filtered.length / this.itemsPerPageStatistics);
    this.currentPageStatistics = Math.min(this.currentPageStatistics, this.totalPagesStatistics);
    this.filteredStatistics = filtered.slice((this.currentPageStatistics - 1) * this.itemsPerPageStatistics, this.currentPageStatistics * this.itemsPerPageStatistics);
  }

  onPageChangeStatistics(page: number): void {
    this.currentPageStatistics = page;
    this.filterStatistics();
  }

  onSearchTermChangeUsers(newSearchTerm: string): void {
    this.searchTermUsers = newSearchTerm;
    this.currentPageUsers = 1;
    this.filterUsers();
  }

  onSearchTermChangeProducts(newSearchTerm: string): void {
    this.searchTermProducts = newSearchTerm;
    this.currentPageProducts = 1;
    this.filterProducts();
  }

  onSearchTermChangeCategories(newSearchTerm: string): void {
    this.searchTermCategories = newSearchTerm;
    this.currentPageCategories = 1;
    this.filterCategories();
  }

  onSearchTermChangeSubcategories(newSearchTerm: string): void {
    this.searchTermSubcategories = newSearchTerm;
    this.currentPageSubcategories = 1;
    this.filterSubcategories();
  }

  onSearchTermChangeOrders(newSearchTerm: string): void {
    this.searchTermOrders = newSearchTerm;
    this.currentPageOrders = 1;
    this.filterOrders();
  }

  onSearchTermChangeCodePromos(newSearchTerm: string): void {
    this.searchTermCodePromos = newSearchTerm;
    this.currentPageCodePromos = 1;
    this.filterCodePromos();
  }

  onSearchTermChangeStatistics(newSearchTerm: string): void {
    this.searchTermStatistics = newSearchTerm;
    this.currentPageStatistics = 1;
    this.filterStatistics();
  }
}
