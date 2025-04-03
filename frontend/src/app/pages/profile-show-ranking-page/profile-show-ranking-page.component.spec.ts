import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileShowRankingPageComponent } from './profile-show-ranking-page.component';

describe('ProfileShowRankingPageComponent', () => {
  let component: ProfileShowRankingPageComponent;
  let fixture: ComponentFixture<ProfileShowRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileShowRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileShowRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
