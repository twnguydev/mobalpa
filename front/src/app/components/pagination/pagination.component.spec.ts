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

    // Задаем начальные данные для пагинации
    component.totalPages = 5;
    component.currentPage = 3;
    component.pages = [1, 2, 3, 4, 5];

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  // Проверка кликабельности кнопки "Première"
  it('should call changePage with 1 when "Première" button is clicked', () => {
    spyOn(component, 'changePage');
    const compiled = fixture.nativeElement as HTMLElement;
    const firstButton = compiled.querySelector('button') as HTMLButtonElement;
    
    firstButton.click();
    expect(component.changePage).toHaveBeenCalledWith(1);
  });

  // Проверка кликабельности кнопки "Précédent"
  it('should call changePage with currentPage - 1 when "Précédent" button is clicked', () => {
    spyOn(component, 'changePage');
    const compiled = fixture.nativeElement as HTMLElement;
    const prevButton = compiled.querySelectorAll('button')[1] as HTMLButtonElement;
    
    prevButton.click();
    expect(component.changePage).toHaveBeenCalledWith(2); // currentPage - 1 (3 - 1 = 2)
  });

  // Проверка кликабельности страницы (например, страницы 3)
  it('should call changePage with page number when page button is clicked', () => {
    spyOn(component, 'changePage');

    const compiled = fixture.nativeElement as HTMLElement;
    // const button = compiled.querySelector('button');
    const pageButton = Array.from(compiled.querySelectorAll('button')).find(button =>
      button.textContent?.trim() === "3") as HTMLButtonElement // Третья кнопка — это страница 3

    expect(pageButton).toBeTruthy();
    pageButton.click();
  });

  it('should call changePage with currentPage + 1 when "Suivant" button is clicked', () => {
    spyOn(component, 'changePage');

    // Ищем кнопку "Suivant" по тексту
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const nextButton = Array.from(buttons).find(button => button.textContent?.includes('Suivant'));

    expect(nextButton).toBeTruthy(); // Проверяем, что кнопка найдена
    nextButton?.click(); // Кликаем по кнопке

    // Проверяем, что changePage вызван с currentPage + 1
    expect(component.changePage).toHaveBeenCalledWith(component.currentPage + 1);
  });

  // Проверка кликабельности кнопки "Dernière"
  it('should call changePage with totalPages when "Dernière" button is clicked', () => {
    spyOn(component, 'changePage');

    // Ищем кнопку "Dernière" по тексту
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const lastButton = Array.from(buttons).find(button => button.textContent?.includes('Dernière'));

    expect(lastButton).toBeTruthy(); // Проверяем, что кнопка найдена
    lastButton?.click(); // Кликаем по кнопке

    expect(component.changePage).toHaveBeenCalledWith(component.totalPages); // Проверяем вызов с totalPages
  });

  // Проверка на отключение кнопки "Précédent" на первой странице
  it('should disable "Précédent" button on first page', () => {
    component.currentPage = 1;
    fixture.detectChanges(); // Обновляем DOM

    const compiled = fixture.nativeElement as HTMLElement;
    const prevButton = compiled.querySelectorAll('button')[1] as HTMLButtonElement;
    
    expect(prevButton.disabled).toBeTrue();
  });

  it('should disable "Suivant" button on last page', () => {
    // Устанавливаем текущую страницу на последнюю
    component.currentPage = 5; // Текущая страница = totalPages
    fixture.detectChanges(); // Обновляем DOM после изменения currentPage

    // Ищем кнопку "Suivant" по тексту
    const compiled = fixture.nativeElement as HTMLElement;
    const buttons = compiled.querySelectorAll('button');
    const nextButton = Array.from(buttons).find(button => button.textContent?.includes('Suivant')) as HTMLButtonElement;

    expect(nextButton).toBeTruthy(); // Проверяем, что кнопка существует
    expect(nextButton.disabled).toBeTrue(); // Проверяем, что кнопка отключена
  });
});
