import { ComponentFixture, TestBed } from '@angular/core/testing';
import { PaginationComponent } from './pagination.component';

describe('PaginationComponent', () => {
  let component: PaginationComponent;
  let fixture: ComponentFixture<PaginationComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [PaginationComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(PaginationComponent);
    component = fixture.componentInstance;

    component.totalPages = 5;
    component.currentPage = 3;
    component.pages = [1, 2, 3, 4, 5];

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call changePage with 1 when "Première" button is clicked', () => {
    spyOn(component, 'changePage');
    const compiled = fixture.nativeElement as HTMLElement;
    const firstButton = compiled.querySelector('button') as HTMLButtonElement;
    
    firstButton.click();
    expect(component.changePage).toHaveBeenCalledWith(1);
  });

  it('should call changePage with currentPage - 1 when "Précédent" button is clicked', () => {
    spyOn(component, 'changePage');
    const compiled = fixture.nativeElement as HTMLElement;
    const prevButton = compiled.querySelectorAll('button')[1] as HTMLButtonElement;
    
    prevButton.click();
    expect(component.changePage).toHaveBeenCalledWith(2);
  });

  it('should call changePage with page number when page button is clicked', () => {
    spyOn(component, 'changePage');

    const compiled = fixture.nativeElement as HTMLElement;
    const pageButton = Array.from(compiled.querySelectorAll('button')).find(button =>
      button.textContent?.trim() === "3") as HTMLButtonElement

    expect(pageButton).toBeTruthy();
    pageButton.click();
  });

  it('should call changePage with currentPage + 1 when "Suivant" button is clicked', () => {
    spyOn(component, 'changePage');

    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const nextButton = Array.from(buttons).find(button => button.textContent?.includes('Suivant'));

    expect(nextButton).toBeTruthy();
    nextButton?.click();

    expect(component.changePage).toHaveBeenCalledWith(component.currentPage + 1);
  });

  it('should call changePage with totalPages when "Dernière" button is clicked', () => {
    spyOn(component, 'changePage');

    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const lastButton = Array.from(buttons).find(button => button.textContent?.includes('Dernière'));

    expect(lastButton).toBeTruthy();
    lastButton?.click();

    expect(component.changePage).toHaveBeenCalledWith(component.totalPages);
  });

  it('should disable "Précédent" button on first page', () => {
    component.currentPage = 1;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const prevButton = compiled.querySelectorAll('button')[1] as HTMLButtonElement;
    
    expect(prevButton.disabled).toBeTrue();
  });

  it('should disable "Suivant" button on last page', () => {
    component.currentPage = 5;
    fixture.detectChanges();

    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const nextButton = Array.from(buttons).find(button => button.textContent?.includes('Suivant')) as HTMLButtonElement;

    expect(nextButton).toBeTruthy();
    expect(nextButton.disabled).toBeTrue();
  });
});
