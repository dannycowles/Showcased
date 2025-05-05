import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileCharacterRankingPageComponent } from './profile-character-ranking-page.component';

describe('ProfileCharacterRankingPageComponent', () => {
  let component: ProfileCharacterRankingPageComponent;
  let fixture: ComponentFixture<ProfileCharacterRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileCharacterRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileCharacterRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
