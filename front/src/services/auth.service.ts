import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import type { IUser } from '@interfaces/user.interface';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = `${environment.apiUrl}/users`;
  public user: IUser | null = null;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    }).pipe(
      map(response => {
        if (response && response.accessToken && response.user) {
          localStorage.setItem('token', response.accessToken);
          localStorage.setItem('currentUser', JSON.stringify(response.user));
          this.user = response.user;
        }
        return response;
      }),
      catchError(error => {
        console.error('Login error', error);
        return throwError(error);
      })
    );
  }

  signup(data: IUser): Observable<any> {
    if (!this.checkInputsSignup(data)) {
      console.error('Invalid signup data');
      return of(null);
    }

    return this.http.post<any>(`${this.apiUrl}/register`, {
      ...data,
      birthdate: new Date(data.birthdate).toISOString()
    }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    }).pipe(
      map(response => {
        localStorage.setItem('token', response.token);
        return response;
      }),
      catchError(error => {
        console.error('Signup error', error);
        return of(null);
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

  checkInputsSignup(data: IUser): boolean | '' | null | undefined {
    const birthdateRegex = new RegExp(/^\d{4}-\d{2}-\d{2}$/);
    const emailRegex = new RegExp(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/);
    const phoneRegex = new RegExp(/^\d{10}$/);
    return (
      data.firstname.length > 3 &&
      data.lastname.length > 3 &&
      emailRegex.test(data.email) &&
      (data.password.length > 5 && data.password === data.confirmPassword) &&
      birthdateRegex.test(data.birthdate) &&
      data.phoneNumber && phoneRegex.test(data.phoneNumber)
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('currentUser');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }

  getAuthHeaders(): HttpHeaders | null {
    const token: string | null = localStorage.getItem('token');
    if (!token || token == null) return null;
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
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
