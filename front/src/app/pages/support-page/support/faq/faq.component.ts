import { Component, Input } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
@Component({
  selector: 'app-faq',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './faq.component.html',
  styleUrl: './faq.component.css'
})
export class FaqComponent {
  @Input() searchQuery: string = '';
  noResult: boolean = false;
  faqs = [
    {
      question: 'Quels types de produits vendez-vous ?',
      answer: 'Nous vendons actuellement des électroménagers, petits et grands, ainsi que des meubles pour la cuisine et d\'autres pièces de la maison.'
    },
    {
      question: 'Comment puis-je passer une commande ?',
      answer: 'Pour passer une commande, ajoutez simplement les produits souhaités à votre panier, puis suivez les étapes de paiement. Vous recevrez une confirmation par e-mail une fois votre commande validée.'
    },
    {
      question: 'Quels sont les délais de livraison ?',
      answer: 'Les délais de livraison varient en fonction de votre localisation et du produit commandé. En général, les livraisons prennent entre 3 et 7 jours ouvrables.'
    },
    {
      question: 'Comment puis-je retourner un produit ?',
      answer: 'Pour retourner un produit, veuillez contacter notre service client dans un délai de 14 jours suivant la réception. Nous vous fournirons les instructions pour effectuer le retour.'
    },
    {
      question: 'Puis-je échanger un produit ?',
      answer: 'Oui, vous pouvez échanger un produit sous 14 jours après réception, à condition qu\'il soit dans son état d\'origine. Contactez notre service client pour organiser l\'échange.'
    },
    {
      question: 'Quels modes de paiement acceptez-vous ?',
      answer: 'Nous acceptons les paiements par carte bancaire, PayPal, et virement bancaire. Toutes les transactions sont sécurisées.'
    },
    {
      question: 'Puis-je suivre ma commande en ligne ?',
      answer: 'Oui, après avoir passé votre commande, vous recevrez un numéro de suivi par e-mail. Vous pouvez utiliser ce numéro pour suivre votre commande sur notre site.'
    },
    {
      question: 'Offrez-vous des réductions pour les commandes en gros ?',
      answer: 'Oui, nous offrons des remises pour les commandes en gros. Veuillez contacter notre service commercial pour plus d\'informations.'
    },
    {
      question: 'Comment puis-je modifier ou annuler ma commande ?',
      answer: 'Pour modifier ou annuler votre commande, veuillez contacter notre service client dès que possible. Si la commande n\'a pas encore été expédiée, nous pourrons procéder à la modification ou à l\'annulation.'
    },
    {
      question: 'Proposez-vous un service de montage pour les meubles ?',
      answer: 'Oui, nous proposons un service de montage pour nos meubles. Vous pouvez sélectionner cette option lors du passage de votre commande.'
    },
    {
      question: 'Comment puis-je utiliser un code promotionnel ?',
      answer: 'Vous pouvez entrer votre code promotionnel lors du processus de paiement dans la section dédiée. La réduction sera appliquée automatiquement.'
    },
    {
      question: 'Vos produits sont-ils couverts par une garantie ?',
      answer: 'Oui, tous nos produits sont couverts par une garantie standard de 2 ans. Certains articles peuvent bénéficier d\'une garantie prolongée. Consultez la fiche produit pour plus de détails.'
    },
    {
      question: 'Puis-je obtenir une facture pour ma commande ?',
      answer: 'Oui, une facture est automatiquement envoyée à votre adresse e-mail une fois votre commande expédiée. Vous pouvez également la télécharger depuis votre compte client.'
    },
    {
      question: 'Comment puis-je contacter le service client ?',
      answer: 'Vous pouvez contacter notre service client par e-mail, téléphone ou via le formulaire de contact sur notre site. Nous sommes disponibles du lundi au vendredi, de 9h à 18h.'
    },
    {
      question: 'Proposez-vous des options de financement ?',
      answer: 'Oui, nous proposons des options de financement pour certaines commandes. Vous pouvez choisir un paiement en plusieurs fois lors du processus de paiement.'
    },
    {
      question: 'Que faire si je reçois un produit défectueux ?',
      answer: 'Si vous recevez un produit défectueux, contactez notre service client dès que possible avec une photo du produit défectueux. Nous organiserons un remplacement ou un remboursement selon vos préférences.'
    }
  ];

  filteredFaqs() {
    if (!this.searchQuery) {
      this.noResult = false;
      return this.faqs;
    }

    const keywords = this.searchQuery.toLowerCase().split(' ');
    const filtered = this.faqs.filter(faq =>

      keywords.every(keyword =>
        faq.question.toLowerCase().includes(keyword) ||
        faq.answer.toLowerCase().includes(keyword)
      )
    );
    this.noResult = filtered.length === 0;
    return filtered;
  }
}
