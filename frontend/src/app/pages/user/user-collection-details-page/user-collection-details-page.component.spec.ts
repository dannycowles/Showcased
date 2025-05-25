import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionDetailsPageComponent } from './user-collection-details-page.component';

describe('UserCollectionDetailsPageComponent', () => {
  let component: UserCollectionDetailsPageComponent;
  let fixture: ComponentFixture<UserCollectionDetailsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCollectionDetailsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserCollectionDetailsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
