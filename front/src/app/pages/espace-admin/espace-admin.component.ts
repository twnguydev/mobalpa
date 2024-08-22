import { Component } from '@angular/core';
import { UtilisateurComponent } from './utilisateur/utilisateur.component';
import { ProduitsComponent } from './produits/produits.component';
import { CategorieComponent } from './categorie/categorie.component';
import { SousCategorieComponent } from './sous-categorie/sous-categorie.component';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-espace-admin',
  standalone: true,
  imports: [UtilisateurComponent, ProduitsComponent, CategorieComponent, SousCategorieComponent,CommonModule],
  templateUrl: './espace-admin.component.html',
  styleUrl: './espace-admin.component.css'
})
export class EspaceAdminComponent {
  tabs = [
    { title: 'Utilisateur' },
    { title: 'Produits' },
    { title: 'Catégorie' },
    { title: 'Sous-Catégorie' },
  ];
  selectedTab: number = 0;
  selectTab(index: number) {
    this.selectedTab = index;
  }
}
