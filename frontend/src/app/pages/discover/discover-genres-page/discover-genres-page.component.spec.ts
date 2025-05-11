import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DiscoverGenresPageComponent } from './discover-genres-page.component';

describe('DiscoverGenresPageComponent', () => {
  let component: DiscoverGenresPageComponent;
  let fixture: ComponentFixture<DiscoverGenresPageComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [DiscoverGenresPageComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DiscoverGenresPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
