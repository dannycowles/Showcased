import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserEpisodeRankingPageComponent } from './user-episode-ranking-page.component';

describe('UserEpisodeRankingPageComponent', () => {
  let component: UserEpisodeRankingPageComponent;
  let fixture: ComponentFixture<UserEpisodeRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserEpisodeRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserEpisodeRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
