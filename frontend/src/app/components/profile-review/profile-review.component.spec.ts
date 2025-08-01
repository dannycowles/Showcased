import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileReviewComponent } from './profile-review.component';

describe('ProfileReviewComponent', () => {
  let component: ProfileReviewComponent;
  let fixture: ComponentFixture<ProfileReviewComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileReviewComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileReviewComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
