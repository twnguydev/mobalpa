import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { map, switchMap, catchError } from 'rxjs/operators';
import { Observable, of } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { IProduct, ICampaign } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiUrl: string = `${environment.apiUrl}/catalogue`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  getProducts(): Observable<IProduct[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();

    return this.http.get<IProduct[]>(`${this.apiUrl}/products`, { headers }).pipe(
      switchMap(products => {
        return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
          map(campaigns => {
            return products.map(product => ({
              ...product,
              campaigns: campaigns.filter(campaign => campaign.targetUuid === product.uuid || campaign.targetUuid === product.subcategory.uuid)
            }));
          })
        );
      }),
      catchError(error => {
        console.error('Error fetching products with campaigns', error);
        return of([]);
      })
    );
  }

  createProduct(product: IProduct): Observable<IProduct> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.post<IProduct>(`${this.apiUrl}/products`, product, { headers });
  }

  getProductById(productId: string): Observable<IProduct | null> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<IProduct>(`${this.apiUrl}/products/${productId}`, { headers }).pipe(
      switchMap(product => {
        return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
          map(campaigns => ({
            ...product,
            campaigns: campaigns.filter(campaign => campaign.targetUuid === product.uuid || campaign.targetUuid === product.subcategory.uuid)
          }))
        );
      }),
      catchError(error => {
        console.error('Error fetching product by ID with campaigns', error);
        return of(null);
      })
    );
  }

  getCategories(): Observable<ICategory[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();

    return this.http.get<ICategory[]>(`${this.apiUrl}/categories`, { headers }).pipe(
      switchMap(categories => {
        return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
          map(campaigns => {
            return categories.map(category => ({
              ...category,
              campaigns: campaigns.filter(campaign => campaign.targetUuid === category.uuid)
            }));
          })
        );
      }),
      catchError(error => {
        console.error('Error fetching categories with campaigns', error);
        return of([]);
      })
    );
  }

  getProductsBySubcategoryUri(
    categoryUri: string,
    subcategoryUri: string,
    filters?: { brandName?: string; color?: string; maxPrice?: number }
  ): Observable<IProduct[]> {
    const fullUri = `${categoryUri}/${subcategoryUri}`;
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();

    return this.getSubcategories().pipe(
      map(subcategories => subcategories.find(subcategory => subcategory.uri === fullUri)),
      switchMap(subcategory => {
        if (!subcategory) {
          return of([]);
        }

        let params = new HttpParams();
        if (filters?.brandName) {
          params = params.set('brandName', filters.brandName);
        }
        if (filters?.color) {
          params = params.set('color', filters.color);
        }
        if (filters?.maxPrice) {
          params = params.set('maxPrice', filters.maxPrice.toString());
        }

        return this.http.get<IProduct[]>(`${this.apiUrl}/subcategories/${subcategory.uuid}/products`, { headers, params }).pipe(
          switchMap(products => {
            return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
              map(campaigns => {
                return products.map(product => ({
                  ...product,
                  campaigns: campaigns.filter(campaign => campaign.targetUuid === product.uuid || campaign.targetUuid === product.subcategory.uuid)
                }));
              })
            );
          }),
          catchError(error => {
            console.error('Error fetching products by subcategory with campaigns', error);
            return of([]);
          })
        );
      }),
      catchError(error => {
        console.error('Error fetching products by subcategory', error);
        return of([]);
      })
    );
  }

  getSubcategoriesByCategoryUri(categoryUri: string): Observable<{ parentCategoryName: string, subcategories: ISubcategory[] }> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();

    return this.getCategories().pipe(
      map(categories => categories.find(category => category.uri === categoryUri)),
      switchMap(category => {
        if (!category) {
          return of({ parentCategoryName: '', subcategories: [] });
        }
        return this.http.get<ICategory>(`${this.apiUrl}/categories/${category.uuid}`, { headers }).pipe(
          switchMap(categoryResponse => {
            return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
              map(campaigns => ({
                parentCategoryName: categoryResponse.name,
                subcategories: categoryResponse.subcategories.map(subcategory => ({
                  ...subcategory,
                  campaigns: campaigns.filter(campaign => campaign.targetUuid === subcategory.uuid || campaign.targetUuid === subcategory.category.uuid)
                }))
              }))
            );
          })
        );
      }),
      catchError(error => {
        console.error('Error fetching subcategories by category with campaigns', error);
        return of({ parentCategoryName: '', subcategories: [] });
      })
    );
  }

  getSubcategories(): Observable<ISubcategory[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<ISubcategory[]>(`${this.apiUrl}/subcategories`, { headers }).pipe(
      switchMap(subcategories => {
        return this.http.get<ICampaign[]>(`${this.apiUrl}/campaigns`, { headers }).pipe(
          map(campaigns => {
            return subcategories.map(subcategory => ({
              ...subcategory,
              campaigns: campaigns.filter(campaign => campaign.targetUuid === subcategory.uuid || campaign.targetUuid === subcategory.category.uuid)
            }));
          })
        );
      }),
      catchError(error => {
        console.error('Error fetching subcategories with campaigns', error);
        return of([]);
      })
    );
  }

  getSubcategoryByUri(categoryUri: string, subcategoryUri: string): Observable<ISubcategory | null> {
    const fullUri = `${categoryUri}/${subcategoryUri}`;
    return this.getSubcategories().pipe(
      map(subcategories => subcategories.find(subcategory => subcategory.uri === fullUri) || null),
      catchError(error => {
        console.error('Error fetching subcategory by URI', error);
        return of(null);
      })
    );
  }

  getProductByUri(categoryUri: string, subcategoryUri: string, productUri: string): Observable<IProduct | null> {
    const fullUri = `${categoryUri}/${subcategoryUri}/${productUri}`;
    return this.getProducts().pipe(
      map(products => products.find(product => product.uri === fullUri) || null),
      catchError(error => {
        console.error('Error fetching product by URI', error);
        return of(null);
      })
    );
  }

  getProductsWithCampaign(): Observable<IProduct[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    return this.http.get<IProduct[]>(`${this.apiUrl}/products-with-campaigns`, { headers });
  }
}