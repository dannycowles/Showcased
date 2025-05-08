import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCharacterRankingPageComponent } from './user-character-ranking-page.component';

describe('UserCharacterRankingPageComponent', () => {
  let component: UserCharacterRankingPageComponent;
  let fixture: ComponentFixture<UserCharacterRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCharacterRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserCharacterRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
