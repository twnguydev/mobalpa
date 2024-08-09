import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormsModule, FormBuilder, FormGroup, Validators, ReactiveFormsModule } from '@angular/forms';

@Component({
  selector: 'app-support',
  standalone: true,
  imports: [CommonModule, FormsModule, ReactiveFormsModule],
  templateUrl: './support.component.html',
  styleUrls: ['./support.component.css']  // Correction ici
})
export class SupportComponent implements OnInit {

  supForm!: FormGroup;
  feedForm!: FormGroup;
  submissionSuccess = false;

  constructor(private fb: FormBuilder) { }

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
    { title: 'Support' },
    { title: 'Votre avis' }
  ];

  ratings: number[] = [1, 2, 3, 4, 5, 6, 7, 8, 9, 10];
  feedback = {
    rating: 0,
    comment: ''
  };

  selectedTab: number = 0;

  selectTab(index: number) {
    this.selectedTab = index;
  }

  onSubmit() {
    this.supForm.markAllAsTouched();  // Marque tous les champs comme touchés pour afficher les erreurs

    if (this.supForm.invalid) {
      return;
    }

    console.log(this.supForm.value);
    this.submissionSuccess = true;

    this.supForm.reset();

    setTimeout(() => {
      this.submissionSuccess = false;
    }, 5000);
  }

  onFeedSubmit() {
    this.feedForm.markAllAsTouched();  
    if (this.feedForm.invalid) {
      return;
    }

    console.log(this.feedForm.value);
    this.feedForm.reset();
    this.submissionSuccess = true;

    setTimeout(() => {
      this.submissionSuccess = false;
    }, 5000);
  }
}
