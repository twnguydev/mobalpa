import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { IUser } from '@interfaces/user.interface';
import { AuthService } from '@services/auth.service';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';


@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl: string = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getAllUsers(): Observable<IUser[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser[]>();
    return this.http.get<IUser[]>(`${this.apiUrl}/users`, { headers });
  }

  getAllProducts(): Observable<IProduct[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IProduct[]>();
    return this.http.get<IProduct[]>(`${this.apiUrl}/products`, { headers });
  }

  getAllCategories(): Observable<ICategory[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ICategory[]>();
    return this.http.get<ICategory[]>(`${this.apiUrl}/categories`, { headers });
  }

  getAllSubcategories(): Observable<ISubcategory[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ISubcategory[]>();
    return this.http.get<ISubcategory[]>(`${this.apiUrl}/subcategories`, { headers });
  }

  createCategory(category: ICategory): Observable<ICategory> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ICategory>();
    return this.http.post<ICategory>(`${this.apiUrl}/categories`, category, { headers });
  }


}
