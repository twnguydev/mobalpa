import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IOrder, ICouponCodeResponse } from '@interfaces/order.interface';
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

  testPromoCode(code: string): Observable<ICouponCodeResponse> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders();
    if (!headers) return new Observable<ICouponCodeResponse>();
    return this.http.post<ICouponCodeResponse>(`${this.orderUrl}/${this.authService.user?.uuid}/apply-coupon`, {
      'couponCode': code
    }, { headers });
  }

  saveTempOrder(order: IOrder): Observable<IOrder> {
    this.tempOrder = order;
    // Simule une requête HTTP pour sauvegarder la commande (vous pouvez remplacer par une vraie requête)
    return new Observable<IOrder>(observer => {
      observer.next(order);
      observer.complete();
    });
  }

  getTempOrder(): IOrder | null {
    return this.tempOrder;
  }

  updateTempOrder(updatedFields: Partial<IOrder>): void {
    if (this.tempOrder) {
      this.tempOrder = { ...this.tempOrder, ...updatedFields };
    }
  }
}