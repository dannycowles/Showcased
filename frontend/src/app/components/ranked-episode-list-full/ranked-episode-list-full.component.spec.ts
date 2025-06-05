import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankedEpisodeListFullComponent } from './ranked-episode-list-full.component';

describe('RankedEpisodeListFullComponent', () => {
  let component: RankedEpisodeListFullComponent;
  let fixture: ComponentFixture<RankedEpisodeListFullComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RankedEpisodeListFullComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RankedEpisodeListFullComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
