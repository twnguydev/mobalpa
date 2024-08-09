import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {
  private apiUrl: string = `${environment.apiUrl}/emails/newsletter`;

  constructor(private http: HttpClient) {}

  addNewsletter(data: { emailUser: string }): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.apiUrl, data, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    });
  }
}
