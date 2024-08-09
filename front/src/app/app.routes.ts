import { RouterModule, Routes } from '@angular/router';
import { NgModule } from '@angular/core';
import { LandingPageComponent } from './pages/landing-page/landing-page.component';
import { CartPageComponent } from './pages/cart_page/cart-page.component';
import { Wishlist_PageComponent } from './pages/wishlist-page/wishlist-page.component';
import { SupportPageComponent } from './pages/support-page/support-page.component';

export const routes: Routes = [
    {
        path: '',
        component: LandingPageComponent,
    },
    {
        path: 'panier',
        component: CartPageComponent,
    },

     {
        path: 'liste-de-souhaits',
         component: Wishlist_PageComponent,
     },

    {
        path: 'support',
        component: SupportPageComponent,
    }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
