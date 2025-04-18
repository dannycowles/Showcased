import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileFollowingPageComponent } from './profile-following-page.component';

describe('ProfileFollowingPageComponent', () => {
  let component: ProfileFollowingPageComponent;
  let fixture: ComponentFixture<ProfileFollowingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileFollowingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileFollowingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
