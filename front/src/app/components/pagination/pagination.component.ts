import { Component, Input, Output, EventEmitter, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-pagination',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './pagination.component.html',
  styleUrls: ['./pagination.component.css']
})
export class PaginationComponent implements OnChanges {
  @Input() currentPage: number = 1;
  @Input() itemsPerPage: number = 10;
  @Input() totalItems: number = 0;
  @Output() pageChange = new EventEmitter<number>();

  totalPages: number = 1;
  pages: (number | string)[] = [];

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['totalItems'] || changes['itemsPerPage'] || changes['currentPage']) {
      this.calculatePages();
      if (this.currentPage > this.totalPages) {
        this.currentPage = this.totalPages || 1;
        this.pageChange.emit(this.currentPage);
      }
    }
  }

  calculatePages(): void {
    this.totalPages = Math.ceil(this.totalItems / this.itemsPerPage);

    const maxPagesToShow = 5;
    const visiblePages: (number | string)[] = [];

    if (this.totalPages <= maxPagesToShow) {
      this.pages = Array.from({ length: this.totalPages }, (_, i) => i + 1);
      return;
    }

    const startPage = Math.max(1, this.currentPage - Math.floor(maxPagesToShow / 2));
    const endPage = Math.min(this.totalPages, startPage + maxPagesToShow - 1);

    if (startPage > 1) {
      visiblePages.push(1);
      if (startPage > 2) {
        visiblePages.push('...');
      }
    }

    for (let i = startPage; i <= endPage; i++) {
      visiblePages.push(i);
    }

    if (endPage < this.totalPages) {
      if (endPage < this.totalPages - 1) {
        visiblePages.push('...');
      }
      visiblePages.push(this.totalPages);
    }

    this.pages = visiblePages;
  }

  changePage(page: number | string): void {
    if (page === '...') {
      return;
    }

    const pageNumber = page as number;

    if (pageNumber >= 1 && pageNumber <= this.totalPages && pageNumber !== this.currentPage) {
      this.currentPage = pageNumber;
      this.pageChange.emit(pageNumber);
    }
  }
}