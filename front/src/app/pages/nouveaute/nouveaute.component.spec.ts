import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NouveauteComponent } from './nouveaute.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { RouterTestingModule } from '@angular/router/testing';

describe('NouveauteComponent', () => {
  let component: NouveauteComponent;
  let fixture: ComponentFixture<NouveauteComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NouveauteComponent, HttpClientTestingModule, RouterTestingModule]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NouveauteComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
