import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class NewsletterService {
  private apiUrl: string = `${environment.apiUrl}/newsletter`;

  constructor(private http: HttpClient) {}

  addNewsletter(userUuid: string): Observable<{ message: string }> {
    return this.http.post<{ message: string }>(this.apiUrl, { user_uuid: userUuid }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    });
  }

  isSubscribed(userUuid: string): Observable<{ isSubscribed: boolean }> {
    return this.http.get<{ isSubscribed: boolean }>(`${this.apiUrl}/is-subscribed/${userUuid}`, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    });
  }
}
