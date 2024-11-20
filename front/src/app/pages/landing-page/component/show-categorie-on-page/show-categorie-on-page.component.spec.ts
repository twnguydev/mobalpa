import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowCategorieOnPageComponent } from './show-categorie-on-page.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ShowCategorieOnPageComponent', () => {
  let component: ShowCategorieOnPageComponent;
  let fixture: ComponentFixture<ShowCategorieOnPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowCategorieOnPageComponent,HttpClientTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowCategorieOnPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
