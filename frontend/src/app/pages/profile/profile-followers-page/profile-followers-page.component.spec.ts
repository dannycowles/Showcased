import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileFollowersPageComponent } from './profile-followers-page.component';

describe('ProfileFollowersPageComponent', () => {
  let component: ProfileFollowersPageComponent;
  let fixture: ComponentFixture<ProfileFollowersPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileFollowersPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileFollowersPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
