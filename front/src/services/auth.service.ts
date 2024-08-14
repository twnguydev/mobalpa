import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
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

  constructor(private http: HttpClient, private router: Router) {
    this.loadUserFromLocalStorage();
    this.checkTokenExpiration();
  }

  private loadUserFromLocalStorage(): void {
    const storedUser: string | null = localStorage.getItem('currentUser');
    if (storedUser) {
      this.user = JSON.parse(storedUser);
    }
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
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
    this.user = null;
    clearTimeout(this.tokenExpirationTimeout);
    this.router.navigate(['/auth/connexion']);
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

  validResetPassword(token: string, newPassword: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/reset-password?token=${token}`, { newPassword });
  }
}