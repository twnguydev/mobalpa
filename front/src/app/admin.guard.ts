import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class AdminGuard implements CanActivate {

  constructor(private authService: AuthService, private router: Router) {}

  canActivate(): Observable<boolean> {
    return this.authService.getCurrentUser().pipe(
        map(currentUser => {
            console.log('Current user:', currentUser);
            if (currentUser && currentUser.roles && currentUser.roles.some((role: any) => role.name === 'ROLE_ADMIN')) {
                return true;
            } else {
                this.router.navigate(['/']);
                return false;
            }
        })
    );
  }
}
