import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WishlistPageComponent } from './wishlist-page.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('WishlistPageComponent', () => {
  let component: WishlistPageComponent;
  let fixture: ComponentFixture<WishlistPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WishlistPageComponent, HttpClientTestingModule, RouterTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(WishlistPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
