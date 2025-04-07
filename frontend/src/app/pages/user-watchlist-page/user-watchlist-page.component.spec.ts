import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserWatchlistPageComponent } from './user-watchlist-page.component';

describe('UserWatchlistPageComponent', () => {
  let component: UserWatchlistPageComponent;
  let fixture: ComponentFixture<UserWatchlistPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserWatchlistPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserWatchlistPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
