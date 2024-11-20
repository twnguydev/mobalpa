import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewsletterSendComponent } from './newsletter-send.component';

describe('NewsletterSendComponent', () => {
  let component: NewsletterSendComponent;
  let fixture: ComponentFixture<NewsletterSendComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewsletterSendComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewsletterSendComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
