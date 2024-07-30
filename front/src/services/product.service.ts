import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { IProduct } from '@interfaces/product.interface';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl: string = 'http://localhost:8081/api/catalogue/products';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getProducts(): Observable<IProduct[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.get<IProduct[]>(this.apiUrl, { headers });
  }

  createProduct(product: IProduct): Observable<IProduct> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.post<IProduct>(this.apiUrl, product, { headers });
  }
}
