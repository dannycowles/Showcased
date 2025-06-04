import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileShowReviewComponent } from './profile-show-review.component';

describe('ProfileShowReviewComponent', () => {
  let component: ProfileShowReviewComponent;
  let fixture: ComponentFixture<ProfileShowReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileShowReviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileShowReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
