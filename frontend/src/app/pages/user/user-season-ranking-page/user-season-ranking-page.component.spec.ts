import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSeasonRankingPageComponent } from './user-season-ranking-page.component';

describe('UserSeasonRankingPageComponent', () => {
  let component: UserSeasonRankingPageComponent;
  let fixture: ComponentFixture<UserSeasonRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSeasonRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSeasonRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
