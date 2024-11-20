import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IOrder, ICouponCodeResponse } from '@interfaces/order.interface';
import { IPayment } from '@interfaces/payment.interface';
import { IWishlist, IWishlistItem } from '@interfaces/wishlist.interface';
import { IDeliveryResponse } from '@interfaces/order.interface';
import { AuthService } from '@services/auth.service';
import { environment } from '@env/environment';

@Injectable({
  providedIn: 'root'
})
export class OrderService {
  private orderUrl: string = `${environment.apiUrl}/orders`;
  private paymentUrl: string = `${environment.apiUrl}/payments`;
  private wishlistUrl: string = `${environment.apiUrl}/wishlist`;
  private tempOrder: IOrder | null = null;

  constructor(private http: HttpClient, private authService: AuthService) {}

  createOrder(order: IOrder): Observable<IOrder> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    if (!headers) return new Observable<IOrder>();
    return this.http.post<IOrder>(this.orderUrl, order, { headers });
  }

  completeOrder(orderUuid: string): Observable<IOrder> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    if (!headers) return new Observable<IOrder>();
    return this.http.post<IOrder>(`${this.orderUrl}/${orderUuid}/payment`, {}, { headers });
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

  getInvoiceByOrderUuid(orderUuid: string | undefined): Observable<Blob> {
    if (!orderUuid) return new Observable<Blob>();
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
  
  saveTempOrder(order: IOrder): void {
    this.tempOrder = order;
    localStorage.setItem('tempOrder', JSON.stringify(order));
  }

  getTempOrder(): IOrder | null {
    if (!this.tempOrder) {
      const tempOrder = localStorage.getItem('tempOrder');
      if (tempOrder) {
        this.tempOrder = JSON.parse(tempOrder);
      }
    }
    return this.tempOrder;
  }

  clearTempOrder(): void {
    this.tempOrder = null;
    localStorage.removeItem('tempOrder');
    localStorage.removeItem('cart');
  }

  updateTempOrder(updatedFields: Partial<IOrder>): void {
    if (this.tempOrder) {
      this.tempOrder = { ...this.tempOrder, ...updatedFields };
      localStorage.setItem('tempOrder', JSON.stringify(this.tempOrder));
    }
  }

  getDeliveryOptions(): Observable<IDeliveryResponse> {
    const headers: HttpHeaders | null = this.authService.getAuthHeaders() ?? this.authService.getXApiKeyHeaders();
    if (!headers) return new Observable<IDeliveryResponse>();
    return this.http.get<IDeliveryResponse>(`${this.orderUrl}/delivery-prices`, { headers });
  }
}