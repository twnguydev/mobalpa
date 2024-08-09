import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpErrorResponse } from '@angular/common/http';
import { Observable, of, throwError } from 'rxjs';
import { catchError, map, switchMap } from 'rxjs/operators';
import type { IUser } from '@interfaces/user.interface';
import { environment } from '../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      }
    }).pipe(
      map(response => {
        if (response && response.token) {
          localStorage.setItem('token', response.token);
        }
        return response;
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Login error', error);
        if (error.status === 400) {
          return throwError('Email ou mot de passe incorrect.');
        } else if (error.status === 404) {
          return throwError('Utilisateur non trouvé.');
        } else if (error.status === 500) {
          return throwError('Une erreur de serveur est survenue. Veuillez réessayer plus tard.');
        } else if (error.status === 409) {
          return throwError('Les données fournies sont invalides. Veuillez vérifier les champs et réessayer.');
        } else if (error.status === 401) {
          return throwError('Email ou mot de passe incorrect.');
        } else if (error.status === 403) {
          return throwError('Accès refusé. Veuillez confirmer votre compte.');
        } else if (error.status === 503) {
          return throwError('Service temporairement indisponible. Veuillez réessayer plus tard.');
        } else {
          return throwError('Une erreur est survenue pendant la connexion. Veuillez réessayer.');
        }
      })

    );
  }




  signup(data: IUser): Observable<string> {
    if (!this.checkInputsSignup(data)) {
      console.error('Données d\'inscription invalides');
      return of('');
    }

    return this.http.post<string>(`${this.apiUrl}/register`, {
      ...data,
      birthdate: new Date(data.birthdate).toISOString(),
    }, {
      headers: {
        'Content-Type': 'application/json',
        'X-API-KEY': `${environment.apiKey}`
      },
      responseType: 'text' as 'json'
    }).pipe(
      switchMap((response) => {
        return of(response);
      }),
      catchError((error: HttpErrorResponse) => {
        console.error('Erreur d\'inscription', error);
        if (error.status === 400) {
          return throwError('Cet email est déjà utilisé. Veuillez utiliser un autre email.');
        } else if (error.status === 409) {
          return throwError('Les données fournies sont invalides. Veuillez vérifier les champs et réessayer.');
        } else if (error.status === 500) {
          return throwError('Une erreur de serveur est survenue. Veuillez réessayer plus tard.');
        } else {
          return throwError('Une erreur est survenue pendant l\'inscription. Veuillez réessayer.');
        }
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

  resetPassword(token: string, newPassword: string, confirmPassword: string): Observable<string> {
    if (newPassword !== confirmPassword) {
      return throwError('Passwords do not match');
    }

    return this.http.post(`${this.apiUrl}/reset-password?token=${token}`, { newPassword, confirmPassword }, {
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

  getAuthHeaders(): HttpHeaders {
    const token: string | null = localStorage.getItem('token');
    return new HttpHeaders({
      'Authorization': `Bearer ${token}`,
      'Content-Type': 'application/json'
    });
  }



}
