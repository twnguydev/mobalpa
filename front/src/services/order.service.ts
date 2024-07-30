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
    const headers = this.authService.getAuthHeaders();
    return this.http.post<IOrder>(this.orderUrl, order, { headers });
  }

  getOrders(): Observable<IOrder[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.get<IOrder[]>(this.orderUrl, { headers });
  }

  getOrderById(orderId: string): Observable<IOrder> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.get<IOrder>(`${this.orderUrl}/${orderId}`, { headers });
  }

  getPayments(): Observable<IPayment[]> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.get<IPayment[]>(this.paymentUrl, { headers });
  }

  createPayment(payment: IPayment): Observable<IPayment> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.post<IPayment>(this.paymentUrl, payment, { headers });
  }

  getWishlist(): Observable<IWishlist> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.get<IWishlist>(this.wishlistUrl, { headers });
  }

  addToWishlist(item: IWishlistItem): Observable<IWishlist> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.post<IWishlist>(this.wishlistUrl, item, { headers });
  }

  removeFromWishlist(itemId: string): Observable<IWishlist> {
    const headers: HttpHeaders = this.authService.getAuthHeaders();
    return this.http.delete<IWishlist>(`${this.wishlistUrl}/${itemId}`, { headers });
  }
}
