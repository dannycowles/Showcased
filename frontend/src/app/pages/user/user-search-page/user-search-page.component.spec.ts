import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserSearchPageComponent } from './user-search-page.component';

describe('UserSearchPageComponent', () => {
  let component: UserSearchPageComponent;
  let fixture: ComponentFixture<UserSearchPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserSearchPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserSearchPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
