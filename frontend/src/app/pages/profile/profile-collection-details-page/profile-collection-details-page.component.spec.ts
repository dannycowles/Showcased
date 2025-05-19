import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileCollectionDetailsPageComponent } from './profile-collection-details-page.component';

describe('ProfileCollectionDetailsPageComponent', () => {
  let component: ProfileCollectionDetailsPageComponent;
  let fixture: ComponentFixture<ProfileCollectionDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileCollectionDetailsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileCollectionDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
