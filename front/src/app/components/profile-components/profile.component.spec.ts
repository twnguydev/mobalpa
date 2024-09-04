import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ProfileComponent } from './profile.component';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { AuthService } from '@services/auth.service';
// import { AuthService } from '../../../services/auth.service';
import { of } from 'rxjs';

describe('ProfileComponent', () => {
  let component: ProfileComponent;
  let fixture: ComponentFixture<ProfileComponent>;

  const mockAuthService = {
    currentUser$: of({ id: 1, name: 'Test User' }),
    isAuthenticated: jasmine.createSpy('isAuthenticated').and.returnValue(of(true))
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        ProfileComponent,
        HttpClientTestingModule,

      ],
      providers: [
        {provide: AuthService, useValue: mockAuthService},
      ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
