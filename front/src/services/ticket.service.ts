import { Injectable } from '@angular/core';
import { environment } from '@env/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@services/auth.service';

export interface TicketRequestDTO {
  userUuid: string;
  type: string;
  name: string;
  issue: string;
}

@Injectable({
  providedIn: 'root'
})
export class TicketService {
  private apiUrl: string = `${environment.apiUrl}/support/ticket`

  constructor(private http: HttpClient, private authService: AuthService) { }

  createTicket(ticketRequest: TicketRequestDTO): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.post<any>(this.apiUrl, ticketRequest, { headers });
  }
}