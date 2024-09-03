import { Component, OnInit } from '@angular/core';
import { AdminService } from '@services/admin.service';
import { IUser } from '@interfaces/user.interface';
import { ICampaign, IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { IOrder } from '@interfaces/order.interface';
import { ICoupon } from '@interfaces/coupon.interface';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { PaginationComponent } from '@components/pagination/pagination.component';
import { Router } from '@angular/router';

@Component({
  selector: 'app-espace-admin',
  standalone: true,
  imports: [CommonModule, FormsModule, PaginationComponent],
  templateUrl: './espace-admin.component.html',
  styleUrls: ['./espace-admin.component.css']
})
export class EspaceAdminComponent implements OnInit {
  tabs = [
    { title: 'Utilisateurs' },
    { title: 'Produits' },
    { title: 'Catégories' },
    { title: 'Sous-catégories' },
    { title: 'Commandes' },
    { title: 'Codes promotionnels' },
    { title: 'Campagnes' },
    { title: 'Service après vente' },
    { title: 'Forecast' },
    { title: 'Newsletters' },
  ];
  selectedTab: number = 0;
  isFormVisible = false;
  showAllUsers: Boolean = false;
  successMessage: string = '';
  errorMessage: string = '';
  showDropdown: boolean = false;

  // Variables pour les utilisateurs
  users: IUser[] = [];
  filteredUsers: IUser[] = [];
  searchTermUsers: string = '';
  currentPageUsers: number = 1;
  itemsPerPageUsers: number = 10;
  totalPagesUsers: number = 1;
  selectedUsers: IUser[] = [];
  searchTerm: string = '';


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
  newCoupon: ICoupon = {
    name: '',
    discountRate: 0,
    discountType: 'PERCENTAGE',
    dateStart: '',
    dateEnd: '',
    targetType: 'ALL_USERS',
    targetUsers: [],
    maxUse: 1
  };

  // Variables pour les statistiques
  forecast: any[] = [];
  summary: any[] = [];
  sales: any[] = [];
  reportType = 'weekly';
  endDate: string = new Date().toISOString().split('T')[0];
  startDate: string = new Date(new Date().setDate(new Date().getDate() - 7)).toISOString().split('T')[0];
  filteredForecast: any[] = [];
  searchTermForecast: string = '';
  currentPageForecast: number = 1;
  itemsPerPageForecast: number = 10;
  totalPagesForecast: number = 1;

  // Variables pour les campagnes
  campaigns: ICampaign[] = [];
  filteredCampaigns: any[] = [];
  searchTermCampaigns: string = '';
  currentPageCampaigns: number = 1;
  itemsPerPageCampaigns: number = 10;
  totalPagesCampaigns: number = 1;
  newCampaign: ICampaign = {
    id: '',
    name: '',
    discountRate: 0,
    dateStart: new Date(),
    dateEnd: new Date(),
    targetUuid: '',
    type: 'CATEGORY'
  };
  campaignTargets: (IProduct | ICategory | ISubcategory)[] = [];
  filteredCampaignTargets: (IProduct | ICategory | ISubcategory)[] = [];
  searchTermCampaignTarget: string = '';
  showCampaignTargetDropdown: boolean = false;
  selectedCampaignTarget: IProduct | ICategory | ISubcategory | null = null;

  // Variables pour les tickets de support
  supportTickets: any[] = [];
  filteredTickets: any[] = [];
  searchTermTickets: string = '';
  currentPageSupportTickets: number = 1;
  itemsPerPageSupportTickets: number = 10;
  totalPagesSupportTickets: number = 1;

  // Variables pour les abonnées newsletter
  subscribers: any[] = [];
  filteredSubscribers: any[] = [];
  searchTermSubscribers: string = '';
  currentPageSubscribers: number = 1;
  itemsPerPageSubscribers: number = 10;
  totalPagesSubscribers: number = 1;



  constructor(private adminService: AdminService, private router: Router) { }

  ngOnInit(): void {
    this.loadUsers();
    this.loadCampaignTargets();
  }
  // Fonction pour changer de tab
  selectTab(index: number) {
    this.selectedTab = index;
    switch (index) {
      case 0: this.loadUsers(); break;
      case 1: this.loadProducts(); break;
      case 2: this.loadCategories(); break;
      case 3: this.loadSubcategories(); break;
      case 4: this.loadOrders(); break;
      case 5: this.loadCodePromos(); break;
      case 6: this.loadCampaigns(); break;
      case 7: this.loadSupportTickets(); break;
      case 8: this.loadForecast(); this.loadSummary(); this.loadSales(); break;
      case 9: this.loadSubscribers(); break;

    }
  }

  // Users
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
  onSearchTermChangeUsers(newSearchTerm: string): void {
    this.searchTermUsers = newSearchTerm;
    this.currentPageUsers = 1;
    this.filterUsers();
    this.showDropdown = this.filteredUsers.length > 0;

  }
  onPageChangeUsers(page: number): void {
    this.currentPageUsers = page;
    this.filterUsers();
  }
  selectUser(user: IUser): void {
    if (!this.selectedUsers.some(u => u.uuid === user.uuid)) {
      this.selectedUsers.push(user);
      this.newCoupon.targetUsers?.push(user.uuid);
    }
    this.searchTermUsers = '';
    this.showDropdown = false;
    this.filterUsers();
  }
  removeUser(user: IUser): void {
    this.selectedUsers = this.selectedUsers.filter(u => u.uuid !== user.uuid);
    this.newCoupon.targetUsers = this.newCoupon.targetUsers?.filter(uuid => uuid !== user.uuid);
    this.filterUsers();
  }
  onInputFocus(): void {
    this.showDropdown = this.filteredUsers.length > 0;
  }
  onInputBlur(): void {
    setTimeout(() => {
      this.showDropdown = false;
    }, 200);
  }
  toggleDropdown(): void {
    this.showDropdown = !this.showDropdown;
  }

  // Products
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
  onSearchTermChangeProducts(newSearchTerm: string): void {
    this.searchTermProducts = newSearchTerm;
    this.currentPageProducts = 1;
    this.filterProducts();
  }
  onPageChangeProducts(page: number): void {
    this.currentPageProducts = page;
    this.filterProducts();
  }

  // Categories
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
  onSearchTermChangeCategories(newSearchTerm: string): void {
    this.searchTermCategories = newSearchTerm;
    this.currentPageCategories = 1;
    this.filterCategories();
  }
  onPageChangeCategories(page: number): void {
    this.currentPageCategories = page;
    this.filterCategories();
  }

  // Subcategories
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
  onSearchTermChangeSubcategories(newSearchTerm: string): void {
    this.searchTermSubcategories = newSearchTerm;
    this.currentPageSubcategories = 1;
    this.filterSubcategories();
  }
  onPageChangeSubcategories(page: number): void {
    this.currentPageSubcategories = page;
    this.filterSubcategories();
  }

  // Orders
  loadOrders(): void {
    this.adminService.getAllOrders().subscribe(orders => {
      this.orders = orders;
      this.filterOrders();
    });
  }
  filterOrders(): void {
    const filtered = this.orders.filter(order =>
      order.uuid?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.user?.firstname.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.user?.lastname.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.totalTtc?.toString().toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.deliveryAddress?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||

      order.status?.toLowerCase().includes(this.searchTermOrders.toLowerCase()) ||
      order.createdAt?.toLowerCase().includes(this.searchTermOrders.toLowerCase())
    );

    this.totalPagesOrders = Math.ceil(filtered.length / this.itemsPerPageOrders);
    this.currentPageOrders = Math.min(this.currentPageOrders, this.totalPagesOrders);
    this.filteredOrders = filtered.slice((this.currentPageOrders - 1) * this.itemsPerPageOrders, this.currentPageOrders * this.itemsPerPageOrders);
  }
  onSearchTermChangeOrders(newSearchTerm: string): void {
    this.searchTermOrders = newSearchTerm;
    this.currentPageOrders = 1;
    this.filterOrders();
  }
  onPageChangeOrders(page: number): void {
    this.currentPageOrders = page;
    this.filterOrders();
  }

  // Code Promos
  loadCodePromos(): void {
    this.adminService.getAllCoupons().subscribe(codePromos => {
      this.codePromos = codePromos;
      this.filterCodePromos();
    });

  }
  deleteCoupon(id: string): void {
    if (confirm('Voulez-vous vraiment supprimer ce code promo ?')) {
      const couponToDelete = this.codePromos.find(coupon => coupon.id === id);

      if (couponToDelete) {
        const couponName = couponToDelete.name || couponToDelete.code;

        this.adminService.deleteCoupon(id).subscribe({
          next: () => {
            this.successMessage = `Le coupon "${couponName}" a été supprimé avec succès.`;
            this.loadCodePromos();

            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
          },
          error: (error) => {
            this.errorMessage = `Erreur lors de la suppression du coupon "${couponName}".`;

            setTimeout(() => {
              this.errorMessage = '';
            }, 3000);
          }
        });
      }
    }
  }
  createCoupon(): void {
    if (typeof this.newCoupon.targetUsers === 'string') {
      this.newCoupon.targetUsers = (this.newCoupon.targetUsers as string)
        .split(',')
        .map(id => id.trim());
    }

    this.newCoupon.dateStart += ":00";
    this.newCoupon.dateEnd += ":00";

    this.adminService.createCoupon(this.newCoupon).subscribe({
      next: () => {
        this.successMessage = 'Coupon créé avec succès';
        this.loadCodePromos();

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);

        this.newCoupon = {
          name: '',
          discountRate: 0,
          discountType: 'PERCENTAGE',
          dateStart: '',
          dateEnd: '',
          targetType: 'ALL_USERS',
          targetUsers: [],
          maxUse: 1
        };
        this.selectedUsers = [];
        this.isFormVisible = false;
      },
      error: (error: any) => {
        this.errorMessage = `Erreur lors de la création du coupon.`;

        setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    });
  }
  filterCodePromos(): void {
    const filtered = this.codePromos.filter(codePromo =>
      (codePromo.code?.toLowerCase() || '').includes(this.searchTermCodePromos.toLowerCase()) ||
      (codePromo.description?.toLowerCase() || '').includes(this.searchTermCodePromos.toLowerCase())

    );

    this.totalPagesCodePromos = Math.ceil(filtered.length / this.itemsPerPageCodePromos);
    this.currentPageCodePromos = Math.min(this.currentPageCodePromos, this.totalPagesCodePromos);
    this.filteredCodePromos = filtered.slice((this.currentPageCodePromos - 1) * this.itemsPerPageCodePromos, this.currentPageCodePromos * this.itemsPerPageCodePromos);
  }
  onPageChangeCodePromos(page: number): void {
    this.currentPageCodePromos = page;
    this.filterCodePromos();
  }
  onSearchTermChangeCodePromos(newSearchTerm: string): void {
    this.searchTermCodePromos = newSearchTerm;
    this.currentPageCodePromos = 1;
    this.filterCodePromos();
  }


  // Campaign
  loadCampaigns(): void {
    this.adminService.getAllCampaigns().subscribe(campaigns => {
      this.campaigns = campaigns;
      this.filterCampaigns();
    });
  }

  createCampaign(): void {
    this.adminService.createCampaign(this.newCampaign).subscribe({
      next: () => {
        console.log('Campagne créée avec succès');
        this.successMessage = 'Campagne créée avec succès';
        this.loadCampaigns();

        setTimeout(() => {
          this.successMessage = '';
        }, 3000);

        this.newCampaign = {
          id: '',
          name: '',
          discountRate: 0,
          dateStart: new Date(),
          dateEnd: new Date(),
          targetUuid: '',
          type: 'PRODUCT'
        };
        this.isFormVisible = false;
      },
      error: (error: any) => {
        this.errorMessage = `Erreur lors de la création de la campagne.`;

        setTimeout(() => {
          this.errorMessage = '';
        }, 3000);
      }
    });
  }

  deleteCampaign(id: string): void {
    if (confirm('Voulez-vous vraiment supprimer cette campagne ?')) {
      const campaignToDelete = this.campaigns.find(campaign => campaign.id === id);

      if (campaignToDelete) {
        const campaignName = campaignToDelete.name;

        this.adminService.deleteCampaign(id).subscribe({
          next: () => {
            this.successMessage = `La campagne "${campaignName}" a été supprimée avec succès.`;
            this.loadCampaigns();

            setTimeout(() => {
              this.successMessage = '';
            }, 3000);
          },
          error: (error) => {
            this.errorMessage = `Erreur lors de la suppression de la campagne "${campaignName}".`;

            setTimeout(() => {
              this.errorMessage = '';
            }, 3000);
          }
        });
      }
    }
  }
  loadCampaignTargets(): void {
    switch (this.newCampaign.type) {
      case 'PRODUCT':
        this.adminService.getAllProducts().subscribe(products => {
          this.campaignTargets = products;
          this.filterCampaignTargets();
        });
        break;
      case 'CATEGORY':
        this.adminService.getAllCategories().subscribe(categories => {
          this.campaignTargets = categories;
          this.filterCampaignTargets();
        });
        break;
      case 'SUBCATEGORY':
        this.adminService.getAllSubcategories().subscribe(subcategories => {
          this.campaignTargets = subcategories;
          this.filterCampaignTargets();
        });
        break;
    }
  }


  filterCampaignTargets(): void {
    if (!this.searchTermCampaignTarget) {
      this.filteredCampaignTargets = this.campaignTargets;
    } else {
      const lowerSearchTerm = this.searchTermCampaignTarget.toLowerCase();
      this.filteredCampaignTargets = this.campaignTargets.filter(target =>
        target.name.toLowerCase().includes(lowerSearchTerm)
      ).sort((a, b) => {
        const aName = a.name.toLowerCase();
        const bName = b.name.toLowerCase();
        return aName.localeCompare(bName);
      });
    }
  }


  onCampaignTargetSearchTermChange(event: Event): void {
    const inputElement = event.target as HTMLInputElement;
    this.searchTermCampaignTarget = inputElement.value;
    this.filterCampaignTargets();
  }

  onInputBlur2(): void {
    setTimeout(() => {
      this.showCampaignTargetDropdown = false;
    }, 200);
  }
  selectCampaignTarget(target: IProduct | ICategory | ISubcategory): void {
    this.selectedCampaignTarget = target;
    this.newCampaign.targetUuid = target.uuid;
    this.searchTermCampaignTarget = '';
    this.showCampaignTargetDropdown = false;
  }

  removeCampaignTarget(): void {
    this.selectedCampaignTarget = null;
    this.newCampaign.targetUuid = '';
  }

  onCampaignTypeChange(): void {
    this.selectedCampaignTarget = null;
    this.newCampaign.targetUuid = '';
    this.loadCampaignTargets();
  }






  filterCampaigns(): void {
    const filtered = this.campaigns;
    this.totalPagesCampaigns = Math.ceil(filtered.length / this.itemsPerPageCampaigns);
    this.currentPageCampaigns = Math.min(this.currentPageCampaigns, this.totalPagesCampaigns);
    this.filteredCampaigns = filtered.slice((this.currentPageCampaigns - 1) * this.itemsPerPageCampaigns, this.currentPageCampaigns * this.itemsPerPageCampaigns);
  }

  onSearchTermChangeCampaigns(newSearchTerm: string): void {
    this.searchTermCampaigns = newSearchTerm;
    this.currentPageCampaigns = 1;
    this.filterCampaigns();
  }

  onPageChangeCampaigns(page: number): void {
    this.currentPageCampaigns = page;
    this.filterCampaigns();
  }

  // Support Tickets
  loadSupportTickets(): void {
    this.adminService.getAllTickets().subscribe(supportTickets => {
      this.supportTickets = supportTickets;
      this.filterTickets();
    });
  }

  filterTickets(): void {
    const filtered = this.supportTickets;
    this.totalPagesSupportTickets = Math.ceil(filtered.length / this.itemsPerPageSupportTickets);
    this.currentPageSupportTickets = Math.min(this.currentPageSupportTickets, this.totalPagesSupportTickets);
    this.filteredTickets = filtered.slice((this.currentPageSupportTickets - 1) * this.itemsPerPageSupportTickets, this.currentPageSupportTickets * this.itemsPerPageSupportTickets);
  }

  onSearchTermChangeSupportTickets(newSearchTerm: string): void {
    this.searchTermTickets = newSearchTerm;
    this.currentPageSupportTickets = 1;
    this.filterTickets();
  }

  onPageChangeSupportTickets(page: number): void {
    this.currentPageSupportTickets = page;
    this.filterTickets();
  }

  // Forecast
  loadForecast(): void {
    this.adminService.getForecast(false, this.reportType).subscribe(forecast => {
      this.forecast = forecast;

      if (this.currentPageForecast < 1) {
        this.currentPageForecast = 1;
      }

      this.filterForecast();
      console.log('Forecast', forecast);
    });
  }

  loadSummary(): void {
    this.adminService.getSummary(false, this.reportType).subscribe(summary => {
      this.summary = summary;
      console.log('Summary', summary);
    });
  }

  loadSales(): void {
    this.adminService.getSales(false, this.startDate, this.endDate).subscribe(sales => {
      this.sales = sales;
      console.log('Sales', sales);
    });
  }

  filterForecast(): void {
    const filtered = this.forecast.slice();
    this.totalPagesForecast = Math.ceil(filtered.length / this.itemsPerPageForecast);

    if (this.currentPageForecast < 1 && this.totalPagesForecast > 0) {
      this.currentPageForecast = 1;
    }

    this.filteredForecast = filtered.slice(
      (this.currentPageForecast - 1) * this.itemsPerPageForecast,
      this.currentPageForecast * this.itemsPerPageForecast
    );
  }

  onReportTypeChange(): void {
    switch (this.reportType) {
      case 'weekly':
        const lastWeek = new Date(new Date().setDate(new Date().getDate() - 7));
        this.endDate = lastWeek.toISOString().split('T')[0];
        this.startDate = new Date(new Date().setDate(lastWeek.getDate() - 7)).toISOString().split('T')[0];
        break;
      case 'monthly':
        const lastMonth = new Date(new Date().setMonth(new Date().getMonth() - 1));
        this.endDate = lastMonth.toISOString().split('T')[0];
        this.startDate = new Date(new Date().setMonth(lastMonth.getMonth() - 1)).toISOString().split('T')[0];
        break;
      case 'yearly':
        const lastYear = new Date(new Date().setFullYear(new Date().getFullYear() - 1));
        this.endDate = lastYear.toISOString().split('T')[0];
        this.startDate = new Date(new Date().setFullYear(lastYear.getFullYear() - 1)).toISOString().split('T')[0];
        break;
    }

    this.loadForecast();
    this.loadSummary();
    this.loadSales();
  }

  // onSearchTermChangeStatistics(newSearchTerm: string): void {
  //   this.searchTermStatistics = newSearchTerm;
  //   this.currentPageStatistics = 1;
  //   this.filterStatistics();
  // }


  onPageChangeForecast(page: number): void {
    this.currentPageForecast = page;
    this.filterForecast();
  }

  downloadCsvSummary(): void {
    this.adminService.getSummary(true, this.reportType).subscribe((response: any) => {
      const blob = new Blob([response], { type: 'text/csv' });
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `summary-${this.reportType}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  downloadCsvForecast(): void {
    this.adminService.getForecast(true, this.reportType).subscribe((response: any) => {
      const blob = new Blob([response], { type: 'text/csv' });
      const url: string = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `forecast-${this.reportType}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  downloadCsvSales(): void {
    this.adminService.getSales(true, this.startDate, this.endDate).subscribe((response: any) => {
      const blob = new Blob([response], { type: 'text/csv' });
      const url: string = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = `sales-${this.startDate}-${this.endDate}.csv`;
      a.click();
      window.URL.revokeObjectURL(url);
    });
  }

  // Subscribers
  loadSubscribers(): void {
    this.adminService.getAllNewsletters().subscribe({
      next: (subscribers) => {
        console.log('Abonnés chargés:', subscribers);
        this.subscribers = subscribers;
        this.filteredSubscribers = subscribers;
      },
      error: (error) => {
        console.error('Erreur lors du chargement des abonnés:', error);
      }
    });
  }


  filterSubscribers(): void {
    const filtered = this.subscribers.filter(subscriber =>
      subscriber.email.toLowerCase().includes(this.searchTermSubscribers.toLowerCase())
    );

    this.totalPagesSubscribers = Math.ceil(filtered.length / this.itemsPerPageSubscribers);
    this.currentPageSubscribers = Math.min(this.currentPageSubscribers, this.totalPagesSubscribers);
    this.filteredSubscribers = filtered.slice((this.currentPageSubscribers - 1) * this.itemsPerPageSubscribers, this.currentPageSubscribers * this.itemsPerPageSubscribers);
  }

  onSearchTermChangeSubscribers(newSearchTerm: string): void {
    this.searchTermSubscribers = newSearchTerm;
    this.currentPageSubscribers = 1;
    this.filterSubscribers();
  }

  onPageChangeSubscribers(page: number): void {
    this.currentPageSubscribers = page;
    this.filterSubscribers();
  }

  deleteSubscriber(uuid: string): void {
    console.log('Tentative de suppression de l\'abonné avec UUID:', uuid);

    if (!uuid) {
      console.error('UUID est undefined');
      return;
    }

    if (confirm('Voulez-vous vraiment supprimer cet abonné ?')) {
      this.adminService.deleteNewsletter(uuid).subscribe({
        next: (response) => {
          console.log('Réponse de la suppression:', response);
          this.successMessage = `L'abonné avec UUID "${uuid}" a été supprimé avec succès.`;
          this.loadSubscribers();

          setTimeout(() => {
            this.successMessage = '';
          }, 3000);
        },
        error: (error) => {
          console.error('Erreur lors de la suppression:', error);
          this.errorMessage = `Erreur lors de la suppression de l'abonné avec UUID "${uuid}".`;

          setTimeout(() => {
            this.errorMessage = '';
          }, 3000);
        }
      });
    }
  }

  viewUserDetails(uuid: string): void {
    this.router.navigate([`/admin/utilisateur/${uuid}`]);
  }


  // Toggle
  toggleFormVisibility() {
    this.isFormVisible = !this.isFormVisible;

  }

  toggleShowAllUsers() {
    this.showAllUsers = !this.showAllUsers;
  }
}



