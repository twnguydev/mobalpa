import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DebugElement } from '@angular/core';
import { By } from '@angular/platform-browser';
import { ArticleMedium } from './article-medium.component';

describe('ProductComponent', () => {
  let component: ArticleMedium;
  let fixture: ComponentFixture<ArticleMedium>;
  let debugElement: DebugElement;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ArticleMedium]
    });

    fixture = TestBed.createComponent(ArticleMedium);
    component = fixture.componentInstance;
    debugElement = fixture.debugElement;
    
  });

  it('Verifie  si les urls dynamique sont présents', () => {
    component.product = {
      product: {
        uri: 'salle-de-bain'
      }
    };

    fixture.detectChanges();

    const anchorElement = debugElement.query(By.css('a')).nativeElement;

    expect(anchorElement.getAttribute('href')).toBe('/categories/salle-de-bain');
  });
  
});
