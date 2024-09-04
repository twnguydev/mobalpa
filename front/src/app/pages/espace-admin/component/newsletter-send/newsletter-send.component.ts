import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, NgForm } from '@angular/forms';
import { NewsletterService } from '@services/newsletter.service';
import { AdminService } from '@services/admin.service';
import { IProduct, ICampaign } from '@interfaces/product.interface';
import { INewsletter, INewsletterSend } from '@interfaces/newsletter.interface';
import { IUser } from '@interfaces/user.interface';

@Component({
  selector: 'app-newsletter-send',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './newsletter-send.component.html',
  styleUrls: ['./newsletter-send.component.css']
})
export class NewsletterSendComponent implements OnInit {
  subject: string = '';
  contentInParagraphStrings: string[] = [];
  newContentParagraph: string = '';
  emails: string[] = [];
  sendDate: string | null = null;

  searchTermProductTarget: string = '';
  showProductTargetDropdown: boolean = false;
  filteredProductTargets: IProduct[] = [];
  selectedProductTargets: IProduct[] = [];

  searchTermCampaignTarget: string = '';
  showCampaignTargetDropdown: boolean = false;
  filteredCampaignTargets: ICampaign[] = [];
  selectedCampaignTarget: ICampaign = {} as ICampaign;

  searchTermSubscriberTarget: string = '';
  showSubscriberTargetDropdown: boolean = false;
  filteredSubscriberTargets: INewsletter[] = [];
  selectedSubscriberTargets: INewsletter[] = [];

  isLoading: boolean = false;
  successMessage: string | null = null;
  errorMessage: string | null = null;

  allProducts: IProduct[] = [];
  allCampaigns: ICampaign[] = [];
  allSubscribers: INewsletter[] = [];

  sendToAllSubscribers: boolean = false;

  constructor(
    private newsletterService: NewsletterService,
    private adminService: AdminService
  ) {}

  ngOnInit(): void {
    this.fetchAllProducts();
    this.fetchAllCampaigns();
    this.fetchAllSubscribers();
  }

  fetchAllProducts(): void {
    this.adminService.getAllProducts().subscribe({
      next: (products) => {
        this.allProducts = products;
        this.filteredProductTargets = products;
      },
      error: (error) => {
        console.error('Error fetching products:', error);
      }
    });
  }

  fetchAllCampaigns(): void {
    this.adminService.getAllCampaigns().subscribe({
      next: (campaigns) => {
        this.allCampaigns = campaigns;
        this.filteredCampaignTargets = campaigns;
      },
      error: (error) => {
        console.error('Error fetching campaigns:', error);
      }
    });
  }

  fetchAllSubscribers(): void {
    this.adminService.getAllNewsletters().subscribe({
      next: (subscribers) => {
        this.allSubscribers = subscribers;
        this.filteredSubscriberTargets = subscribers;
      },
      error: (error) => {
        console.error('Error fetching subscribers:', error);
      }
    });
  }

  addContentParagraph(): void {
    if (this.newContentParagraph.trim()) {
      this.contentInParagraphStrings.push(this.newContentParagraph.trim());
      this.newContentParagraph = '';
    }
  }

  removeContentParagraph(index: number): void {
    this.contentInParagraphStrings.splice(index, 1);
  }

  addEmail(email: string): void {
    if (email.trim() && this.validateEmail(email.trim())) {
      this.emails.push(email.trim());
    }
  }

  removeEmail(email: string): void {
    this.emails = this.emails.filter(e => e !== email);
  }

  validateEmail(email: string): boolean {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]{2,}$/i;
    return emailRegex.test(email);
  }

  onSubscriberTargetSearchTermChange(): void {
    const searchTerm = this.searchTermSubscriberTarget.toLowerCase();
    this.filteredSubscriberTargets = this.allSubscribers.filter(subscriber =>
      subscriber.email.toLowerCase().includes(searchTerm)
    );
  }

  selectSubscriberTarget(subscriber: INewsletter): void {
    if (!this.selectedSubscriberTargets.find(s => s.id === subscriber.id)) {
      this.selectedSubscriberTargets.push(subscriber);
    }
    this.searchTermSubscriberTarget = '';
    this.showSubscriberTargetDropdown = false;
  }

  removeSubscriberTarget(subscriber: INewsletter): void {
    this.selectedSubscriberTargets = this.selectedSubscriberTargets.filter(s => s.id !== subscriber.id);
  }

  onProductTargetSearchTermChange(): void {
    const searchTerm = this.searchTermProductTarget.toLowerCase();
    this.filteredProductTargets = this.allProducts.filter(product =>
      product.name.toLowerCase().includes(searchTerm)
    );
  }

  selectProductTarget(product: IProduct): void {
    if (!this.selectedProductTargets.find(p => p.uuid === product.uuid)) {
      this.selectedProductTargets.push(product);
    }
    this.searchTermProductTarget = '';
    this.showProductTargetDropdown = false;
  }

  removeProductTarget(product: IProduct): void {
    this.selectedProductTargets = this.selectedProductTargets.filter(p => p.uuid !== product.uuid);
  }

  onCampaignTargetSearchTermChange(): void {
    const searchTerm = this.searchTermCampaignTarget.toLowerCase();
    this.filteredCampaignTargets = this.allCampaigns.filter(campaign =>
      campaign.name.toLowerCase().includes(searchTerm)
    );
  }

  selectCampaignTarget(campaign: ICampaign): void {
    this.selectedCampaignTarget = campaign;
    this.searchTermCampaignTarget = '';
    this.showCampaignTargetDropdown = false;
  }

  removeCampaignTarget(): void {
    this.selectedCampaignTarget = {} as ICampaign;
  }

  onSubmit(): void {
    this.isLoading = true;

    if (this.sendToAllSubscribers) {
      this.adminService.getAllNewsletters().subscribe({
        next: (subscribers: INewsletter[]) => {
          this.emails = subscribers.map(subscriber => subscriber.email);
          this.sendNewsletter();
        },
        error: (error) => {
          this.errorMessage = 'Erreur lors de la récupération des emails des abonnés.';
          this.isLoading = false;
        }
      });
    } else {
      if (this.emails.length === 0 && this.selectedSubscriberTargets.length === 0) {
        this.errorMessage = 'Veuillez ajouter au moins un destinataire ou sélectionner "Envoyer à tous les abonnés".';
        this.isLoading = false;
        return;
      }
      this.sendNewsletter();
    }
  }

  sendNewsletter(): void {
    const newsletterData: INewsletterSend = {
      subject: this.subject,
      contentInParagraphStrings: this.contentInParagraphStrings,
      emails: [...this.emails, ...this.selectedSubscriberTargets.map(subscriber => subscriber.email)],
      products: this.selectedProductTargets,
      campaign: this.selectedCampaignTarget,
      sendDate: this.sendDate
    };

    this.adminService.sendNewsletter(newsletterData).subscribe({
      next: (response) => {
        this.successMessage = response.message;
        this.errorMessage = null;
        this.resetForm();
      },
      error: (error) => {
        this.errorMessage = 'Échec de l\'envoi de la newsletter.';
        this.successMessage = null;
      },
      complete: () => {
        this.isLoading = false;
      }
    });
  }

  resetForm(): void {
    this.subject = '';
    this.contentInParagraphStrings = [];
    this.emails = [];
    this.selectedProductTargets = [];
    this.selectedCampaignTarget = {} as ICampaign;
    this.selectedSubscriberTargets = [];
    this.sendDate = null;
    this.sendToAllSubscribers = false;
  }

  onInputBlur(field: string): void {
    setTimeout(() => {
      if (field === 'product') {
        this.showProductTargetDropdown = false;
      } else if (field === 'campaign') {
        this.showCampaignTargetDropdown = false;
      } else if (field === 'subscriber') {
        this.showSubscriberTargetDropdown = false;
      }
    }, 200);
  }
}