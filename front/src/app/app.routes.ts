import { RouterModule, Routes, ExtraOptions } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from './auth.guard';
import { NoAuthGuard } from './no-auth.guard';

import { LandingPageComponent } from '@pages/landing-page/landing-page.component';
import { CartPageComponent } from '@pages/cart_page/cart-page.component';
import { Wishlist_PageComponent } from '@pages/wishlist-page/wishlist-page.component';
import { SupportPageComponent } from '@pages/support-page/support-page.component';
import { SubcategoriePageComponent } from '@pages/subcategorie-page/subcategorie-page.component';
import { ProductComponent } from '@components/product-components/product.component';

import { HomeComponent } from '@components/home-components/home.component';
import { LoginComponent } from '@components/login-components/login.component';
import { RegisterComponent } from '@components/register-components/register.component';
import { ResetPasswordComponent } from '@components/reset-password/reset-password.component';
import { CategoryComponent } from '@components/category-components/category.component';
import { ProfileComponent } from '@components/profile-components/profile.component';
import { LegalNoticeComponent } from '@pages/legal-notice/legal-notice.component';
import { PrivacyPolicyComponent } from '@pages/privacy-policy/privacy-policy.component';
import { TermsAndConditionsComponent } from '@pages/terms-and-conditions/terms-and-conditions.component';
import { TermsOfUseComponent } from '@pages/terms-of-use/terms-of-use.component';

export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'panier', component: CartPageComponent, canActivate: [AuthGuard] },
    { path: 'liste-de-souhaits', component: Wishlist_PageComponent, canActivate: [AuthGuard] },
    { path: 'support', component: SupportPageComponent },
    { path: 'info', component: HomeComponent },
    { path: 'auth/connexion', component: LoginComponent, canActivate: [NoAuthGuard] },
    { path: 'auth/inscription', component: RegisterComponent, canActivate: [NoAuthGuard] },
    { path: 'auth/reinitialiser-mot-de-passe', component: ResetPasswordComponent, canActivate: [NoAuthGuard] },
    { path: 'categories/:categoryUri', component: SubcategoriePageComponent },
    { path: 'categories/:categoryUri/:subcategoryUri', component: CategoryComponent },
    { path: 'categories/:categoryUri/:subcategoryUri/:productUri', component: ProductComponent },
    { path: 'profil', component: ProfileComponent, canActivate: [AuthGuard] },

    { path: 'mentions-legales', component: LegalNoticeComponent },
    { path: 'politique-de-confidentialite', component: PrivacyPolicyComponent },
    { path: 'conditions-generales-de-vente', component: TermsAndConditionsComponent },
    { path: 'conditions-generales-utilisation', component: TermsOfUseComponent },
];

const routerOptions: ExtraOptions = {
    useHash: true,
};

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule]
})
export class AppRoutingModule {}