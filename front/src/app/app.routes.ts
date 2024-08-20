import { RouterModule, Routes, ExtraOptions } from '@angular/router';
import { NgModule } from '@angular/core';
import { LandingPageComponent } from '@pages/landing-page/landing-page.component';
import { CartPageComponent } from '@pages/cart_page/cart-page.component';
import { Wishlist_PageComponent } from '@pages/wishlist-page/wishlist-page.component';
import { SupportPageComponent } from '@pages/support-page/support-page.component';
import { SubcategoriePageComponent } from '@pages/subcategorie-page/subcategorie-page.component';

import { HomeComponent } from '@components/home-components/home.component';
import { LoginComponent } from '@components/login-components/login.component';
import { RegisterComponent } from '@components/register-components/register.component';
import { CategoryComponent } from '@components/category-components/category.component';
import { ResetPasswordComponent } from '@components/reset-password/reset-password.component';
import { ProductComponent } from '@components/product-components/product.component';
import { ProfileComponent } from '@components/profile-components/profile.component';
import { LegalNoticeComponent } from '@pages/legal-notice/legal-notice.component';
import { PrivacyPolicyComponent } from '@pages/privacy-policy/privacy-policy.component';
import { TermsAndConditionsComponent } from '@pages/terms-and-conditions/terms-and-conditions.component';
import { TermsOfUseComponent } from '@pages/terms-of-use/terms-of-use.component';

import { AboutAsComponent } from '@components/footer-pages/about-as/about-as.component';
import { GarantieComponent } from '@components/footer-pages/retour-de-marchandises/garantie.component';
import { CommentRetournerUnProduitComponent } from '@components/footer-pages/retour-de-marchandises/parts/comment-retourner-un-produit/comment-retourner-un-produit.component';
import { DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent } from '@components/footer-pages/retour-de-marchandises/parts/dans-quelles-situations-puis-je-retourner-les-marchandises/dans-quelles-situations-puis-je-retourner-les-marchandises.component';
import { PuisJeRenvoyerTousLesArticlesComponent } from '@components/footer-pages/retour-de-marchandises/parts/puis-je-renvoyer-tous-les-articles/puis-je-renvoyer-tous-les-articles.component';
import { FraisDeRetourComponent } from '@components/footer-pages/retour-de-marchandises/parts/frais-de-retour/frais-de-retour.component';

import { ServiceDeGarantieComponent } from '@components/footer-pages/service-de-garantie/service-de-garantie.component';
import { ReparationSousGarantieComponent } from '@components/footer-pages/service-de-garantie/pages/reparation-sous-garantie/reparation-sous-garantie.component';

export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'panier', component: CartPageComponent },
    { path: 'liste-de-souhaits', component: Wishlist_PageComponent },
    { path: 'support', component: SupportPageComponent },
    { path: 'info', component: HomeComponent },
    { path: 'auth/connexion', component: LoginComponent },
    { path: 'auth/inscription', component: RegisterComponent },
    { path: 'auth/reinitialiser-mot-de-passe', component: ResetPasswordComponent },
    { path: 'categories/:categoryUri', component: SubcategoriePageComponent },
    { path: 'categories/:categoryUri/:subcategoryUri', component: CategoryComponent },
    { path: 'profil', component: ProfileComponent },

    { path: 'mentions-legales', component: LegalNoticeComponent },
    { path: 'politique-de-confidentialite', component: PrivacyPolicyComponent },
    { path: 'conditions-generales-de-vente', component: TermsAndConditionsComponent },
    { path: 'conditions-generales-utilisation', component: TermsOfUseComponent },
    { path: 'à-propos-de', component: AboutAsComponent },

    { path: 'retour-de-marchandises', component: GarantieComponent },
    { path: 'retour-de-marchandises/comment-retourner-un-produit', component: CommentRetournerUnProduitComponent },
    { path: 'retour-de-marchandises/dans-quelles-situations-puis-je-retourner-les-marchandises', component: DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent },
    { path: 'retour-de-marchandises/puis-je-renvoyer-tous-les-articles', component: PuisJeRenvoyerTousLesArticlesComponent },
    { path: 'retour-de-marchandises/frais-de-retour', component: FraisDeRetourComponent },

    { path: 'service-de-garantie', component:ServiceDeGarantieComponent },
    { path: "service-de-garantie/reparation-sous-garantie", component:ReparationSousGarantieComponent }
];

const routerOptions: ExtraOptions = {
    useHash: true,
};

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule]
})
export class AppRoutingModule {}
