import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserFollowingPageComponent } from './user-following-page.component';

describe('UserFollowingPageComponent', () => {
  let component: UserFollowingPageComponent;
  let fixture: ComponentFixture<UserFollowingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserFollowingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserFollowingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
