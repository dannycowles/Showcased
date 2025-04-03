import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileEpisodeRankingPageComponent } from './profile-episode-ranking-page.component';

describe('ProfileEpisodeRankingPageComponent', () => {
  let component: ProfileEpisodeRankingPageComponent;
  let fixture: ComponentFixture<ProfileEpisodeRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileEpisodeRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileEpisodeRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
