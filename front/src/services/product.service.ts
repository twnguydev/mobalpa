import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl: string = `${environment.apiUrl}/catalogue`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getProducts(): Observable<IProduct[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<IProduct[]>(this.apiUrl + '/products', { headers });
  }

  createProduct(product: IProduct): Observable<IProduct> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.post<IProduct>(this.apiUrl + '/products', product, { headers });
  }

  getProductById(productId: string): Observable<IProduct> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<IProduct>(`${this.apiUrl}/products/${productId}`, { headers });
  }

  getCategories(): Observable<ICategory[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    console.log('Headers', headers);
    return this.http.get<ICategory[]>(this.apiUrl + '/categories', { headers });
  }

  getProductsByCategory(categoryUri: string): Observable<IProduct[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<IProduct[]>(`${this.apiUrl}/categories/${categoryUri}/products`, { headers });
  }
}
