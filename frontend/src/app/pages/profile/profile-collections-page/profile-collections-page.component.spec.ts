import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ProfileCollectionsPageComponent } from './profile-collections-page.component';

describe('ProfileCollectionsPageComponent', () => {
  let component: ProfileCollectionsPageComponent;
  let fixture: ComponentFixture<ProfileCollectionsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ProfileCollectionsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ProfileCollectionsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
