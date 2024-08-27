import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { SatisfactionService } from '@services/satisfaction.service';

@Component({
  selector: 'app-notice',
  standalone: true,
  imports: [CommonModule, HttpClientModule],
  templateUrl: './notice.component.html',
  styleUrls: ['./notice.component.css']
})
export class NoticeComponent implements OnInit {

  satisfactions: any[] = [];

  constructor(private satisfactionService: SatisfactionService) {}

  ngOnInit() {
    this.satisfactionService.getLandingPageSatisfaction().subscribe(
      (response) => {
        this.satisfactions = response.map((satisfaction: any) => {
          satisfaction.createdAt = this.formatDate(satisfaction.createdAt);
          return satisfaction;
        });
        console.log('Avis récupérés :', this.satisfactions);
      },
      (error) => {
        console.error('Erreur lors de la récupération des avis :', error);
      }
    );
  }

  formatDate(dateArray: number[]): string {
    if (dateArray.length < 3) {
      return 'Invalid Date';
    }

    const year = dateArray[0];
    const month = dateArray[1] - 1;
    const day = dateArray[2];

    const date = new Date(year, month, day);

    const dayFormatted = date.getDate();
    const monthFormatted = date.toLocaleString('default', { month: 'long' });
    const yearFormatted = date.getFullYear();

    return `${dayFormatted} ${monthFormatted} ${yearFormatted}`;
  }
}
