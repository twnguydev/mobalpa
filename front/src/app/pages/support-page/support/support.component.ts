import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';
import { FaqComponent } from './faq/faq.component';
import { TicketService, TicketRequestDTO } from '@services/ticket.service';
import { SatisfactionService, SatisfactionRequestDTO } from '@services/satisfaction.service';
import { AuthService } from '@services/auth.service';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule, FaqComponent],
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']
})
export class SupportComponent implements OnInit {
  searchQuery: string = '';

  supForm!: FormGroup;
  feedForm!: FormGroup;
  submissionSuccess = false;

  constructor(private fb: FormBuilder, private ticketService: TicketService, private authService: AuthService, private satisfactionService: SatisfactionService, ) { }

  ngOnInit() {
    this.supForm = this.fb.group({
      requestType: ['', Validators.required],
      object: ['', [Validators.required, Validators.minLength(5)]],
      requestContent: ['', [Validators.required, Validators.minLength(10)]]
    });

    this.feedForm = this.fb.group({
      rating: ['', Validators.required],
      comment: ['', [Validators.required, Validators.minLength(10)]]
    });
  }

  tabs = [
    { title: 'FAQ' },
    { title: 'Contact' },
    { title: 'Votre avis' }
  ];

  ratings: number[] = [1, 2, 3, 4, 5];
  feedback = {
    rating: 0,
    comment: ''
  };

  selectedTab: number = 0;

  selectTab(index: number) {
    this.selectedTab = index;
  }

  onSubmit() {
    this.supForm.markAllAsTouched();

    if (this.supForm.invalid) {
      return;
    }

    const userUuid = this.authService.user?.uuid;

    if (!userUuid) {
      console.error('User UUID not found');
      return;
    }

    const ticketRequest: TicketRequestDTO = {
      userUuid: userUuid,
      type: this.supForm.get('requestType')?.value,
      name: this.supForm.get('object')?.value,
      issue: this.supForm.get('requestContent')?.value,
    };

    console.log("ticket:" + ticketRequest.userUuid + "token:" );

    this.ticketService.createTicket(ticketRequest).subscribe(
      response => {
        console.log('Ticket créé avec succès:', response);
        this.submissionSuccess = true;
        this.supForm.reset();

        setTimeout(() => {
          this.submissionSuccess = false;
        }, 5000);
      },
      error => {
        console.error('Erreur lors de la création du ticket:', error);
      }
    );
  }

  onFeedSubmit() {
    this.feedForm.markAllAsTouched();
    if (this.feedForm.invalid) {
      return;
    }

    const userUuid = this.authService.user?.uuid;

    if (!userUuid) {
      console.error('User UUID not found');
      return;
    }

    const satisfactionRequest: SatisfactionRequestDTO = {
      userUuid: userUuid,
      targetType: "MAIN",
      targetUuid: null,
      rating: this.feedForm.get('rating')?.value,
      comment: this.feedForm.get('comment')?.value,
      createdAt: 1
    };

    this.satisfactionService.createSatisfaction(satisfactionRequest).subscribe(
      response => {
        console.log('Satisfaction créée avec succès:', response);
        this.submissionSuccess = true;
        this.feedForm.reset();

        setTimeout(() => {
          this.submissionSuccess = false;
        }, 5000);
      },
      error => {
        console.error('Erreur lors de la création de la satisfaction:', error);
      }
    );
  }
}
