import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, of, throwError } from 'rxjs';
import { MatDialog } from '@angular/material/dialog';
import { tap } from 'rxjs/operators';
import { Router } from '@angular/router';
import { catchError, map, switchMap } from 'rxjs/operators';
import type { IUser } from '@interfaces/user.interface';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = `${environment.apiUrl}/users`;
  public user: IUser | null = null;
  private tokenExpirationTimeout: any;

  private currentUserSubject = new BehaviorSubject<IUser | null>(null);
  currentUser$ = this.currentUserSubject.asObservable();

  private authStatus = new BehaviorSubject<boolean>(this.isAuthenticated());
  authStatus$ = this.authStatus.asObservable();

  constructor(private http: HttpClient, private router: Router, private dialog: MatDialog) {
    this.loadUserFromLocalStorage();
    this.checkTokenExpiration();
  }

  private loadUserFromLocalStorage(): void {
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
    }
  }

  loadUserData(uuid: string): Observable<IUser | null> {
    const token: string | null = localStorage.getItem('token');
    if (!token) {
      return of(null);
    }

    return this.http.get<IUser>(`${this.apiUrl}/${uuid}`, {
      headers: this.getAuthHeaders() as HttpHeaders
    }).pipe(
      map(user => {
        this.currentUserSubject.next(user);
        return user;
      }),
      catchError(error => {
        console.error('Error fetching user data:', error);
        this.clearCurrentUser();
        return of(null);
      })
    );
  }

  getCurrentUser(): Observable<IUser | null> {
    return this.currentUser$;
  }

  clearCurrentUser(): void {
    this.currentUserSubject.next(null);
  }

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    }).pipe(
      tap(response => {
        if (response && response.accessToken && response.user) {
          localStorage.setItem('token', response.accessToken);
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          this.user = response.user;
          this.authStatus.next(true);
          this.setTokenExpiration(response.accessToken);
        }
      })
    );
  }

  signup(data: IUser): Observable<any> {
    const formatDate = (date: string | Date): string => {
      const d = new Date(date);
      const year = d.getFullYear();
      const month = String(d.getMonth() + 1).padStart(2, '0');
      const day = String(d.getDate()).padStart(2, '0');
      return `${year}-${month}-${day}`;
    };

    return this.http.post<any>(`${this.apiUrl}/register`, {
      ...data,
      birthdate: formatDate(data.birthdate)
    }, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      })
    }).pipe(
      map(response => response),
      catchError(error => {
        console.error('Detailed error response:', error);
        if (error.status === 400 && error.error.message) {
          return of({ errors: [error.error.message] });
        }
        return of({ errors: ['Erreur inconnue. Veuillez réessayer.'] });
      })
    );
  }

  forgotPassword(email: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/forgot-password`, { email }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      },
      responseType: 'text' as 'json'
    }).pipe(
      map(response => {
        return response as string;
      }),
      catchError(error => {
        console.error('Forgot password error', error);
        return of('An error occurred');
      })
    );
  }

  resetPassword(token: string, newPassword: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/reset-password`, { newPassword }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      },
      params: { token },
      responseType: 'text' as 'json'
    }).pipe(
      map(response => {
        return response as string;
      }),
      catchError(error => {
        console.error('Reset password error', error);
        return of('An error occurred');
      })
    );
  }

  logout(): void {
    const confirmation = window.confirm('Êtes-vous sûr de vouloir vous déconnecter ?');

    if (confirmation) {
      localStorage.removeItem('token');
      localStorage.removeItem('currentUser');
      this.user = null;
      this.authStatus.next(false);
      clearTimeout(this.tokenExpirationTimeout);
      this.router.navigate(['/auth/connexion']);
    } else {
      console.log("Déconnexion annulée");
    }
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  redirectToLogin(): void {
    this.router.navigate(['/auth/connexion']);
  }

  private setTokenExpiration(token: string): void {
    const expirationDate = this.getTokenExpirationDate(token);
    if (expirationDate) {
      const now = new Date().getTime();
      const expirationTime = expirationDate.getTime() - now;

      if (expirationTime > 0) {
        this.tokenExpirationTimeout = setTimeout(() => {
          this.logout();
        }, expirationTime);
      } else {
        this.logout();
      }
    }
  }

  private getTokenExpirationDate(token: string): Date | null {
    try {
      const decoded = JSON.parse(atob(token.split('.')[1]));
      if (decoded.exp) {
        return new Date(decoded.exp * 1000);
      }
    } catch (error) {
      console.error('Error parsing token', error);
    }
    return null;
  }

  private checkTokenExpiration(): void {
    const token = localStorage.getItem('token');
    if (token) {
      this.setTokenExpiration(token);
    }
  }

  getAuthHeaders(): HttpHeaders | null {
    const token: string | null = localStorage.getItem('token');
    if (token) {
      return new HttpHeaders({
        'Authorization': `Bearer ${token}`
      });
    }
    return null;
  }

  getXApiKeyHeaders(): HttpHeaders {
    return new HttpHeaders({
      'X-API-KEY': `${environment.apiKey}`
    });
  }

  getCurrentUserUuid(): string | null {
    const token: string | null = localStorage.getItem('token');
    if (token) {
      try {
        const payload = token.split('.')[1];
        const decodedPayload = JSON.parse(atob(payload));
        return decodedPayload.uuid;
      } catch (error) {
        console.error('Error decoding token:', error);
        return null;
      }
    }
    return null;
  }

  validResetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/reset-password?token=${token}`, { newPassword });
  }
}