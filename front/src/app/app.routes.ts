import { Routes } from '@angular/router';
import { HomeComponent } from './components/home-components/home.component';
import { LoginComponent } from './components/login-components/login.component';
import { RegisterComponent } from './components/register-components/register.component';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';
import { CategoryComponent } from '@components/category-components/category.component';
import { ProductComponent } from './components/product-components/product.component';

export const routes: Routes = [
    { path: '', component: LandingPageComponent, },
    { path: 'info', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'panier', component: CartPageComponent},
    { path: 'category', component: CategoryComponent },
    { path: 'product', component: ProductComponent },
];
