import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileSeasonRankingPageComponent } from './profile-season-ranking-page.component';

describe('ProfileSeasonRankingPageComponent', () => {
  let component: ProfileSeasonRankingPageComponent;
  let fixture: ComponentFixture<ProfileSeasonRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileSeasonRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileSeasonRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
