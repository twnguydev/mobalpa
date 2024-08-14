import { RouterModule, Routes, ExtraOptions } from '@angular/router';
import { NgModule } from '@angular/core';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';
import { Wishlist_PageComponent } from './pages/wishlist-page/wishlist-page.component';
import { SupportPageComponent } from './pages/support-page/support-page.component';
import { SubcategoriePageComponent } from './pages/subcategorie-page/subcategorie-page.component';

import { HomeComponent } from './components/home-components/home.component';
import { LoginComponent } from './components/login-components/login.component';
import { RegisterComponent } from './components/register-components/register.component';
import { CategoryComponent } from '@components/category-components/category.component';
import { ProductComponent } from './components/product-components/product.component';
import { ProfileComponent } from '@components/profile-components/profile.component';

export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'panier', component: CartPageComponent },
    { path: 'liste-de-souhaits', component: Wishlist_PageComponent },
    { path: 'support', component: SupportPageComponent },
    { path: 'info', component: HomeComponent },
    { path: 'login', component: LoginComponent },
    { path: 'register', component: RegisterComponent },
    { path: 'categories/:categoryUri/:subcategoryUri', component: CategoryComponent },
    { path: 'profile', component: ProfileComponent },
    { path: 'categories/:categoryUri', component: SubcategoriePageComponent },
];

const routerOptions: ExtraOptions = {
    useHash: true,
};

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule]
})
export class AppRoutingModule {}