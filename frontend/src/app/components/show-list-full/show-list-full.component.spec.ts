import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ShowListFullComponent } from './show-list-full.component';

describe('ShowListFullComponent', () => {
  let component: ShowListFullComponent;
  let fixture: ComponentFixture<ShowListFullComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ShowListFullComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(ShowListFullComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
