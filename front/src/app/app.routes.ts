import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AuthGuard } from './auth.guard';

export const routes: Routes = [
  // { path: 'login', component: LoginComponent },
  // { path: '', component: YourHomeComponent, canActivate: [AuthGuard] }, // Ceci est une route protégée par le service AuthGuard
];