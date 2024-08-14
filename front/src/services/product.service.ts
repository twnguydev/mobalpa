import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, switchMap, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
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
  
    return this.getCategories().pipe(
      map(categories => categories.find(category => category.uri === categoryUri)),
      switchMap(category => {
        if (!category) {
          return new Observable<IProduct[]>((observer) => {
            observer.next([]);
            observer.complete();
          });
        }
        return this.http.get<IProduct[]>(`${this.apiUrl}/categories/${category.uuid}/products`, { headers });
      }),
      catchError(error => {
        console.error('Error fetching products by category', error);
        return of([]);
      })
    );
  }

  getSubcategoriesByCategory(categoryUri: string): Observable<{ parentCategoryName: string, subcategories: ISubcategory[] }> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();

    return this.getCategories().pipe(
      map(categories => categories.find(category => category.uri === categoryUri)),
      switchMap(category => {
        if (!category) {
          return new Observable<{ parentCategoryName: string, subcategories: ISubcategory[] }>((observer) => {
            observer.next({ parentCategoryName: '', subcategories: [] });
            observer.complete();
          });
        }
        return this.http.get<ICategory>(`${this.apiUrl}/categories/${category.uuid}`, { headers }).pipe(
          map(categoryResponse => ({
            parentCategoryName: categoryResponse.name,
            subcategories: categoryResponse.subcategories
          }))
        );
      }),
      catchError(error => {
        console.error('Error fetching subcategories by category', error);
        return of({ parentCategoryName: '', subcategories: [] });
      })
    );
  }
}
