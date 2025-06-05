import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankedSeasonListFullComponent } from './ranked-season-list-full.component';

describe('RankedSeasonListFullComponent', () => {
  let component: RankedSeasonListFullComponent;
  let fixture: ComponentFixture<RankedSeasonListFullComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RankedSeasonListFullComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RankedSeasonListFullComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
