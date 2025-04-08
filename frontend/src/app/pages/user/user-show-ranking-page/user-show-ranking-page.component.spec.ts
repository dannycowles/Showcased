import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserShowRankingPageComponent } from './user-show-ranking-page.component';

describe('UserShowRankingPageComponent', () => {
  let component: UserShowRankingPageComponent;
  let fixture: ComponentFixture<UserShowRankingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserShowRankingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserShowRankingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
