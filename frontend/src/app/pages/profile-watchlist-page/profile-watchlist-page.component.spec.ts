import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileWatchlistPageComponent } from './profile-watchlist-page.component';

describe('ProfileWatchlistPageComponent', () => {
  let component: ProfileWatchlistPageComponent;
  let fixture: ComponentFixture<ProfileWatchlistPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileWatchlistPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileWatchlistPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
