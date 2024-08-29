import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { IUser } from '@interfaces/user.interface';
import { AuthService } from '@services/auth.service';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { IOrder } from '@interfaces/order.interface';

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
    return this.http.get<IProduct[]>(`http://localhost:8080/api/catalogue/products`, { headers });
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

  getAllOrders(): Observable<IOrder[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IOrder[]>();
    return this.http.get<IOrder[]>(`${this.apiUrl}/orders`, { headers });
  }


  getAllCoupons(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/coupons`, { headers });
  }

  createCoupon(coupon: any): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.post<any>(`${this.apiUrl}/coupons`, coupon, { headers });
  }

  deleteCoupon(id: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.delete<any>(`${this.apiUrl}/coupons/${id}`, { headers });
  }

  getAllCampaigns(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/campaigns`, { headers });
  }

  getAllTickets(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/ticket`, { headers });
  }


}
