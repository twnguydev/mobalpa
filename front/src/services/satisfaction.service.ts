import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from '@services/auth.service';
import { environment } from '@env/environment';

export interface SatisfactionRequestDTO {
    userUuid: string;
    targetType: string;
    targetUuid: string | null;
    rating: number;
    comment: string;
}

@Injectable({
    providedIn: 'root'
})
export class SatisfactionService {
    private apiUrl = `${environment.apiUrl}/satisfaction/create`;

    constructor(private http: HttpClient, private authService: AuthService) {}

    createSatisfaction(request: SatisfactionRequestDTO): Observable<any> {
        const headers: HttpHeaders | null = this.authService.getAuthHeaders();
        if (!headers) return new Observable<any>();
        return this.http.post<any>(this.apiUrl, request, { headers });
    }
}
