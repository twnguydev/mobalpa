import { Component } from '@angular/core';
import { SupportComponent } from './support/support.component';

@Component({
  selector: 'app-support-page',
  standalone: true,
  imports: [SupportComponent],
  templateUrl: './support-page.component.html',
  styleUrl: './support-page.component.css'
})
export class SupportPageComponent {

}
