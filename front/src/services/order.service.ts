import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IOrder } from '@interfaces/order.interface';
import { IPayment } from '@interfaces/payment.interface';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';
import { AuthService } from '@services/auth.service';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private orderUrl: string = `${environment.apiUrl}/orders`;
  private paymentUrl: string = `${environment.apiUrl}/payments`;
  private wishlistUrl: string = `${environment.apiUrl}/wishlist`;

  constructor(private http: HttpClient, private authService: AuthService) {}

  createOrder(order: Omit<IOrder, 'orderId'>): Observable<IOrder> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IOrder>();
    return this.http.post<IOrder>(this.orderUrl, order, { headers });
  }

  getOrders(): Observable<IOrder[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IOrder[]>();
    return this.http.get<IOrder[]>(this.orderUrl, { headers });
  }

  getOrderById(orderId: string): Observable<IOrder> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<IOrder>();
    return this.http.get<IOrder>(`${this.orderUrl}/${orderId}`, { headers });
  }

  getInvoiceByOrderUuid(orderUuid: string): Observable<Blob> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<Blob>();
    return this.http.get(`${this.orderUrl}/${orderUuid}/invoice`, {
      headers,
      responseType: 'blob',
    });
  }

  testPromoCode(code: string): Observable<string[]> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<string[]>();
    return this.http.post<string[]>(`${this.orderUrl}/${this.authService.user?.uuid}/apply-coupon`, {
      'couponCode': code
    }, { headers });
  }
}
