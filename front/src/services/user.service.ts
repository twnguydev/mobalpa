import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { IUser } from '@interfaces/user.interface';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  private apiUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getAll(): Observable<IUser[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser[]>();
    return this.http.get<IUser[]>(this.apiUrl, { headers });
  }

  getOneBy(userId: string): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.get<IUser>(`${this.apiUrl}/${userId}`, { headers });
  }

  create(user: IUser): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.post<IUser>(this.apiUrl, user, { headers });
  }

  update(user: IUser): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.put<IUser>(`${this.apiUrl}/${user.uuid}`, user, { headers });
  }

  delete(userId: string): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.delete<IUser>(`${this.apiUrl}/${userId}`, { headers });
  }

  getWishlist(userId: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/${userId}/wishlist`, { headers });
  }

  modifyWishlist(userUuid: string, action: 'add' | 'remove', item: Partial<IWishlistItem>): Observable<IWishlist> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IWishlist>();
    const body = { action, item };
    return this.http.patch<IWishlist>(`${this.apiUrl}/${userUuid}/wishlist`, body, { headers });
  }
}
