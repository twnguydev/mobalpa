import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../environments/environment';
import { IUser } from '@interfaces/user.interface';
import { AuthService } from '@services/auth.service';
import { IProduct } from '@interfaces/product.interface';
import { ICategory, ISubcategory } from '@interfaces/category.interface';
import { IOrder } from '@interfaces/order.interface';
import { ICoupon } from '@interfaces/coupon.interface';
import { INewsletter, INewsletterSend } from '@interfaces/newsletter.interface';

@Injectable({
  providedIn: 'root'
})
export class AdminService {
  private apiUrl: string = `${environment.apiUrl}/admin`;

  constructor(private http: HttpClient, private authService: AuthService) { }

  // Users
  getAllUsers(): Observable<IUser[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser[]>();
    return this.http.get<IUser[]>(`${this.apiUrl}/users`, { headers });
  }

  createUser(user: IUser): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.post<IUser>(`${this.apiUrl}/users`, user, { headers });
  }

  updateUser(userId: string, updatedData: Partial<IUser>): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.patch<IUser>(`http://localhost:8080/api/users/${userId}`, updatedData, { headers });
  }
  deleteUser(id: string): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.delete<IUser>(`${this.apiUrl}/users/${id}`, { headers });
  }

  // Products
  getAllProducts(): Observable<IProduct[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IProduct[]>();
    return this.http.get<IProduct[]>(`http://localhost:8080/api/catalogue/products`, { headers });
  }

  // Categories
  getAllCategories(): Observable<ICategory[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ICategory[]>();
    return this.http.get<ICategory[]>(`${this.apiUrl}/categories`, { headers });
  }

  // Subcategories
  getAllSubcategories(): Observable<ISubcategory[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ISubcategory[]>();
    return this.http.get<ISubcategory[]>(`${this.apiUrl}/subcategories`, { headers });
  }

  // Orders
  getAllOrders(): Observable<IOrder[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IOrder[]>();
    return this.http.get<IOrder[]>(`${this.apiUrl}/orders`, { headers });
  }

  // Coupons
  getAllCoupons(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/coupons`, { headers });
  }

  createCoupon(coupon: ICoupon): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.post<any>(`${this.apiUrl}/coupons`, coupon, { headers });
  }

  deleteCoupon(id: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.delete<any>(`${this.apiUrl}/coupons/${id}`, { headers });
  }

  // Campaigns
  getAllCampaigns(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/campaigns`, { headers });
  }

  createCampaign(campaign: any): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.post<any>(`${this.apiUrl}/campaigns`, campaign, { headers });
  }

  deleteCampaign(id: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.delete<any>(`${this.apiUrl}/campaigns/${id}`, { headers });
  }

  // Tickets Support
  getAllTickets(): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/ticket`, { headers });
  }

  createTicket(ticket: any): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.post<any>(`${this.apiUrl}/ticket`, ticket, { headers });
  }

  deleteTicket(id: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.delete<any>(`${this.apiUrl}/ticket/${id}`, { headers });
  }

  updateTicket(ticket: any): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.put<any>(`${this.apiUrl}/ticket/${ticket.id}`, ticket, { headers });
  }

  // Forecast
  getForecast(csv: boolean | string, reportType: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();

    let response;

    if (csv) {
      response = this.http.get(`${this.apiUrl}/forecast/csv?reportType=${reportType}`, {
        headers,
        responseType: 'blob'
      });
    } else {
      response = this.http.get<any>(`${this.apiUrl}/forecast?reportType=${reportType}`, { headers });
    }

    return response;
  }

  getSummary(csv: boolean | string, reportType: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();

    let response;

    if (csv) {
      response = this.http.get(`${this.apiUrl}/summary/csv?reportType=${reportType}`, {
        headers,
        responseType: 'blob'
      });
    } else {
      response = this.http.get<any>(`${this.apiUrl}/summary?reportType=${reportType}`, { headers });
    }

    return response;
  }

  getSales(csv: boolean | string, startDate: string, endDate: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();

    let response;

    if (csv) {
      response = this.http.get(`${this.apiUrl}/sales/csv?startDate=${startDate}&endDate=${endDate}`, {
        headers,
        responseType: 'blob'
      });
    } else {
      response = this.http.get<any>(`${this.apiUrl}/sales?startDate=${startDate}&endDate=${endDate}`, { headers });
    }

    return response;
  }

  getAllNewsletters(): Observable<INewsletter[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<INewsletter[]>();
    return this.http.get<INewsletter[]>(`${this.apiUrl}/newsletters`, { headers });
  }

  deleteNewsletter(uuid: string): Observable<string> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<string>();

    return this.http.delete(`${this.apiUrl}/newsletter/${uuid}`, {
      headers,
      responseType: 'text'
    });
  }

  getUserByUuid(uuid: string): Observable<IUser> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IUser>();
    return this.http.get<IUser>(`${this.apiUrl}/users/details/${uuid}`, { headers });
  }

  getUserCoupons(userUuid: string): Observable<any[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any[]>();
    return this.http.get<any[]>(`${this.apiUrl}/users/${userUuid}/coupons`, { headers });
  }

  getPaymentsByUserUuid(uuid: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/users/${uuid}/payments`, { headers });
  }

  sendNewsletter(newsletterData: INewsletterSend): Observable<{ message: string }> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<{ message: string }>();
    return this.http.post<{ message: string }>(`${this.apiUrl}/newsletter/send`, newsletterData, { headers });
  }

  getTicketByUuid(uuid: string): Observable<any> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<any>();
    return this.http.get<any>(`${this.apiUrl}/ticket/${uuid}`, { headers });
  }
 
  resolveTicket(ticketUuid: string, responderUuid: string, resolution: string): Observable<any> {
    const headers = this.authService.getAuthHeaders();
    if (!headers) {
      return new Observable<any>();
    }
 
    const body = {
      responderUuid: responderUuid,
      resolution: resolution
    };
 
    return this.http.patch<any>(`${this.apiUrl}/ticket/${ticketUuid}/resolve`, body, { headers });
  }
}
