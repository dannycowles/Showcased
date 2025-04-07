import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserWatchingPageComponent } from './user-watching-page.component';

describe('UserWatchingPageComponent', () => {
  let component: UserWatchingPageComponent;
  let fixture: ComponentFixture<UserWatchingPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserWatchingPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserWatchingPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
