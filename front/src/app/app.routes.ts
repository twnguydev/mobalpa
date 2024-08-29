import { RouterModule, Routes, ExtraOptions } from '@angular/router';
import { NgModule } from '@angular/core';
import { AuthGuard } from './auth.guard';
import { NoAuthGuard } from './no-auth.guard';
import { AdminGuard } from './admin.guard';

import { LandingPageComponent } from '@pages/landing-page/landing-page.component';
import { CartPageComponent } from '@pages/cart_page/cart-page.component';
import { Wishlist_PageComponent } from '@pages/wishlist-page/wishlist-page.component';
import { SupportPageComponent } from '@pages/support-page/support-page.component';
import { SubcategoriePageComponent } from '@pages/subcategorie-page/subcategorie-page.component';
import { ProductComponent } from '@components/product-components/product.component';
import { ConfirmOrderComponent } from '@pages/cart_page/cart/component/confirm-order/confirm-order.component';
import { OrderDetailsComponent } from '@pages/cart_page/cart/component/order-details/order-details.component';
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
import { EspaceAdminComponent } from '@pages/espace-admin/espace-admin.component';
import { ContactPageComponent } from '@pages/contact-page/contact-page.component';
import { LivraisonComponent } from '@pages/livraison/livraison.component';
import { QuellesSontLesMethodesDeLivraisonComponent } from '@pages/livraison/quelles-sont-les-methodes-de-livraison/quelles-sont-les-methodes-de-livraison.component';
import { CommentSePasseUneLivraisonComponent } from '@pages/livraison/comment-se-passe-une-livraison/comment-se-passe-une-livraison.component';
import { AnnulerCommandeEnCoursDeLivraisonComponent } from '@pages/livraison/annuler-commande-en-cours-de-livraison/annuler-commande-en-cours-de-livraison.component';
import { QuelleSontLesFraisDeLivraisonComponent } from '@pages/livraison/quelle-sont-les-frais-de-livraison/quelle-sont-les-frais-de-livraison.component';
import { CooperationComponent } from '@pages/cooperation/cooperation.component';
import { FournisseurComponent } from '@pages/fournisseur/fournisseur.component';







import { AboutAsComponent } from '@components/footer-pages/about-as/about-as.component';
import { GarantieComponent } from '@components/footer-pages/retour-de-marchandises/garantie.component';
import { CommentRetournerUnProduitComponent } from '@components/footer-pages/retour-de-marchandises/parts/comment-retourner-un-produit/comment-retourner-un-produit.component';
import { DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent } from '@components/footer-pages/retour-de-marchandises/parts/dans-quelles-situations-puis-je-retourner-les-marchandises/dans-quelles-situations-puis-je-retourner-les-marchandises.component';
import { PuisJeRenvoyerTousLesArticlesComponent } from '@components/footer-pages/retour-de-marchandises/parts/puis-je-renvoyer-tous-les-articles/puis-je-renvoyer-tous-les-articles.component';
import { FraisDeRetourComponent } from '@components/footer-pages/retour-de-marchandises/parts/frais-de-retour/frais-de-retour.component';

import { ServiceDeGarantieComponent } from '@components/footer-pages/service-de-garantie/service-de-garantie.component';
import { ReparationSousGarantieComponent } from '@components/footer-pages/service-de-garantie/parts/reparation-sous-garantie/reparation-sous-garantie.component';
import { QuelsProduitsSontRetournesSousGarantieComponent } from '@components/footer-pages/service-de-garantie/parts/quels-produits-sont-retournes-sous-garantie/quels-produits-sont-retournes-sous-garantie.component';
import { OuDemanderLeServiceDeGarantieComponent } from '@components/footer-pages/service-de-garantie/parts/ou-demander-le-service-de-garantie/ou-demander-le-service-de-garantie.component';
import { QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent } from '@components/footer-pages/service-de-garantie/parts/que-se-passe-t-il-si-le-centre-de-service-ne-peut-pas-reparer-le-produit-pendant-la-periode-de-garantie/que-se-passe-t-il-si-le-centre-de-service-ne-peut-pas-reparer-le-produit-pendant-la-periode-de-garantie.component';
import { DansQuelsCasLaGarantieNEstEllePasAssureeComponent } from '@components/footer-pages/service-de-garantie/parts/dans-quels-cas-la-garantie-n-est-elle-pas-assuree/dans-quels-cas-la-garantie-n-est-elle-pas-assuree.component';

export const routes: Routes = [
    { path: '', component: LandingPageComponent },
    { path: 'panier', component: CartPageComponent },
    { path: 'commande/confirmation', component: ConfirmOrderComponent },
    { path: 'commande/details', component: OrderDetailsComponent },
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
    { path: 'qui-sommes-nous', component: AboutAsComponent },
    { path: 'admin', component: EspaceAdminComponent, canActivate: [AdminGuard] },
    { path: 'contactez-nous', component: ContactPageComponent },
    { path: 'livraison', component: LivraisonComponent },
    { path: 'livraison/quelles-sont-les-methodes-de-livraison', component: QuellesSontLesMethodesDeLivraisonComponent },
    { path: 'livraison/comment-se-passe-une-livraison', component: CommentSePasseUneLivraisonComponent },
    { path: 'livraison/annuler-commande-en-cours-de-livraison', component: AnnulerCommandeEnCoursDeLivraisonComponent },
    { path: 'livraison/quelle-sont-les-frais-de-livraison', component: QuelleSontLesFraisDeLivraisonComponent },
    { path: 'cooperation', component: CooperationComponent },
    { path: 'fournisseur', component: FournisseurComponent },





    { path: 'retour-de-marchandises', component: GarantieComponent },
    { path: 'retour-de-marchandises/comment-retourner-un-produit', component: CommentRetournerUnProduitComponent },
    { path: 'retour-de-marchandises/dans-quelles-situations-puis-je-retourner-les-marchandises', component: DansQuellesSituationsPuisJeRetournerLesMarchandisesComponent },
    { path: 'retour-de-marchandises/puis-je-renvoyer-tous-les-articles', component: PuisJeRenvoyerTousLesArticlesComponent },
    { path: 'retour-de-marchandises/frais-de-retour', component: FraisDeRetourComponent },

    { path: 'service-de-garantie', component:ServiceDeGarantieComponent },
    { path: "service-de-garantie/reparation-sous-garantie", component:ReparationSousGarantieComponent },
    { path: "service-de-garantie/quels-produits-sont-retournes-sous-garantie", component:QuelsProduitsSontRetournesSousGarantieComponent },
    { path: "service-de-garantie/ou-demander-le-service-de-garantie", component:OuDemanderLeServiceDeGarantieComponent },
    { path: "service-de-garantie/que-se-passe-t-il-si-le-centre-de-service-ne-peut-pas-reparer-le-produit-pendant-la-periode-de-garantie", component:QueSePasseTIlSiLeCentreDeServiceNePeutPasReparerLeProduitPendantLaPeriodeDeGarantieComponent },
    { path: "service-de-garantie/dans-quels-cas-la-garantie-n'est-elle-pas-assuree", component: DansQuelsCasLaGarantieNEstEllePasAssureeComponent },
];

const routerOptions: ExtraOptions = {
    useHash: true,
};

@NgModule({
    imports: [RouterModule.forRoot(routes, routerOptions)],
    exports: [RouterModule]
})

export class AppRoutingModule { }
