import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileWatchingPageComponent } from './profile-watching-page.component';

describe('ProfileWatchingPageComponent', () => {
  let component: ProfileWatchingPageComponent;
  let fixture: ComponentFixture<ProfileWatchingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileWatchingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileWatchingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
