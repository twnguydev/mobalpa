import { ComponentFixture, TestBed } from '@angular/core/testing';
import { NoticeComponent } from './notice.component';
import { SatisfactionService } from '@services/satisfaction.service';
import { of } from 'rxjs';
import { HttpClientModule } from '@angular/common/http';

// Создаем мок для SatisfactionService
const mockSatisfactionService = {
  getLandingPageSatisfaction: jasmine.createSpy('getLandingPageSatisfaction').and.returnValue(of([
    { createdAt: [2023, 8, 29], rating: 4, comment: 'Great service!' }
  ]))
};

describe('NoticeComponent', () => {
  let component: NoticeComponent;
  let fixture: ComponentFixture<NoticeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NoticeComponent, HttpClientModule], // Обязательно нужно добавить HttpClientModule
      providers: [
        { provide: SatisfactionService, useValue: mockSatisfactionService } // Предоставляем мокированный сервис
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NoticeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should load satisfactions on init', () => {
    expect(component.satisfactions.length).toBeGreaterThan(0);
    expect(component.satisfactions[0].createdAt).toBe('29 August 2023');
  });

  it('should format date correctly', () => {
    const dateArray = [2023, 8, 29];
    const formattedDate = component.formatDate(dateArray);
    expect(formattedDate).toBe('29 August 2023');
  });

  it('should return correct number of stars', () => {
    const stars = component.getStars(3);
    expect(stars.length).toBe(5);
    expect(stars).toEqual([1, 1, 1, 0, 0]);
  });
});
