import { ComponentFixture, TestBed } from '@angular/core/testing';

import { RankedShowListFullComponent } from './ranked-show-list-full.component';

describe('RankedShowListFullComponent', () => {
  let component: RankedShowListFullComponent;
  let fixture: ComponentFixture<RankedShowListFullComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [RankedShowListFullComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(RankedShowListFullComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
