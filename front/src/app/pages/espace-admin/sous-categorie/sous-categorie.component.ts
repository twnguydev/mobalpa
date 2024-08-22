import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-sous-categorie',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './sous-categorie.component.html',
  styleUrl: './sous-categorie.component.css'
})
export class SousCategorieComponent {
  isModalOpen = false;

  openModal() {
    this.isModalOpen = true;
  }

  closeModal() {
    this.isModalOpen = false;
  }

   isEditOpen = false;

  openEdit() {
    this.isEditOpen = true;
  }

  closeEdit() {
    this.isEditOpen = false;
  }
}
