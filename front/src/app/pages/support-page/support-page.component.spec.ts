import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SupportPageComponent } from './support-page.component';
import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { TicketService } from '@services/ticket.service';

describe('SupportPageComponent', () => {
  let component: SupportPageComponent;
  let fixture: ComponentFixture<SupportPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [SupportPageComponent, HttpClientTestingModule],
      providers: [TicketService]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SupportPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('verifie si le component "support" est chargée dans le component principale ', () => {
    const element: DebugElement = fixture.debugElement.query(By.css('app-support'));
    expect(element).toBeTruthy();
  });
});
