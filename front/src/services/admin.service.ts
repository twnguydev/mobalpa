import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { IUser } from '@interfaces/user.interface';
import { AuthService } from '@services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl: string = `${environment.apiUrl}/admin/users`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getAll(): Observable<IUser[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser[]>();
    return this.http.get<IUser[]>(this.apiUrl, { headers });
  }
}
