import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, of } from 'rxjs';
import { catchError, map } from 'rxjs/operators';
import type { IUserData } from '@interfaces/user.interface';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private apiUrl: string = 'http://localhost:8080/api/users';

  constructor(private http: HttpClient) {}

  login(email: string, password: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, { email, password }).pipe(
      map(response => {
        localStorage.setItem('token', response.token);
        this.fetchUserData(response.token);
        return response;
      }),
      catchError(error => {
        console.error('Login error', error);
        return of(null);
      })
    );
  }

  fetchUserData(token: string): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/me`, {
      headers: {
        Authorization: `Bearer ${token}`
      }
    }).pipe(
      map(user => {
        localStorage.setItem('currentUser', JSON.stringify(user));
        return user;
      }),
      catchError(error => {
        console.error('Fetch user data error', error);
        return of(null);
      })
    );
  }

  signup(data: IUserData): Observable<any> {
    if (!this.checkInputsSignup(data)) {
      console.error('Invalid signup data');
      return of(null);
    }

    return this.http.post<any>(`${this.apiUrl}/signup`, {
      ...data,
      birthdate: new Date(data.birthdate).toISOString()
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

  checkInputsSignup(data: IUserData): boolean | '' | null | undefined {
    const birthdateRegex = new RegExp(/^\d{2}\/\d{2}\/\d{4}$/);
    const emailRegex = new RegExp(/^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/);
    const phoneRegex = new RegExp(/^\d{10}$/);
    return (
      data.firstName.length > 3 &&
      data.lastName.length > 3 &&
      emailRegex.test(data.email) &&
      (data.password.length > 5 && data.password === data.confirmPassword) &&
      birthdateRegex.test(data.birthdate) &&
      data.phone && phoneRegex.test(data.phone) &&
      data.address && data.address.length > 3 &&
      data.city && data.city.length > 3 &&
      data.zipcode && data.zipcode.length > 3 &&
      data.country && data.country.length > 3
    );
  }

  logout(): void {
    localStorage.removeItem('token');
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}