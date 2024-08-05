import { Routes } from '@angular/router';
import { HomeComponent } from './components/home-components/home.component';
import { LoginComponent } from './components/login-components/login.component';
import { RegisterComponent } from './components/register-components/register.component';
import { ResetPasswordComponent } from './components/reset-password/reset-password.component';


export const routes: Routes = [
  { path: '', component: HomeComponent },
  { path: 'login', component: LoginComponent },
  { path: 'register', component: RegisterComponent },
  { path: 'reset-password', component: ResetPasswordComponent },

];
