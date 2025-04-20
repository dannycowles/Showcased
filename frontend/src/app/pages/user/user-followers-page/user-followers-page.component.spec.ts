import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFollowersPageComponent } from './user-followers-page.component';

describe('UserFollowersPageComponent', () => {
  let component: UserFollowersPageComponent;
  let fixture: ComponentFixture<UserFollowersPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserFollowersPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserFollowersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
