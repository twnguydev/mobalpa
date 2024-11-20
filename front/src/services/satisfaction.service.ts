import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, retry } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { environment } from '@env/environment';

export interface SatisfactionRequestDTO {
    userUuid: string;
    targetType: string;
    targetUuid: string | null;
    rating: number;
    comment: string;
    createdAt?: number | null;
}

@Injectable({
    providedIn: 'root'
})
export class SatisfactionService {
    private apiUrl = `${environment.apiUrl}`;

    constructor(private http: HttpClient, private authService: AuthService) {}

    createSatisfaction(request: SatisfactionRequestDTO): Observable<any> {
        const headers: HttpHeaders | null = this.authService.getAuthHeaders();
        if (!headers) return new Observable<any>();
        return this.http.post<any>(this.apiUrl + "/satisfaction/create", request, { headers });
    }

    getLandingPageSatisfaction(): Observable<any> {
        const headers: HttpHeaders | null = this.authService.getAuthHeaders() || this.authService.getXApiKeyHeaders();
        if (!headers) return new Observable<any>();
        return this.http.get<any>(this.apiUrl + "/satisfaction/home", { headers });
    }

    getProductSatisfactions(productUuid: string): Observable<any> {
        const headers: HttpHeaders | null = this.authService.getAuthHeaders() || this.authService.getXApiKeyHeaders();
        if (!headers) return new Observable<any>();
        return this.http.get<any>(this.apiUrl + "/satisfaction/product/" + productUuid, { headers });
    }
}
