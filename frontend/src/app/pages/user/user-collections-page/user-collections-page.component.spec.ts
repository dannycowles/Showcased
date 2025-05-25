import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserCollectionsPageComponent } from './user-collections-page.component';

describe('UserCollectionsPageComponent', () => {
  let component: UserCollectionsPageComponent;
  let fixture: ComponentFixture<UserCollectionsPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserCollectionsPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserCollectionsPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
