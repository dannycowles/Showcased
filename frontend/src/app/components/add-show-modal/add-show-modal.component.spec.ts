import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddShowModalComponent } from './add-show-modal.component';

describe('AddShowModalComponent', () => {
  let component: AddShowModalComponent;
  let fixture: ComponentFixture<AddShowModalComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddShowModalComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AddShowModalComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
