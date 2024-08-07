import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AddUpdateAppealComponent } from './add-update-appeal.component';

describe('AddUpdateAppealComponent', () => {
  let component: AddUpdateAppealComponent;
  let fixture: ComponentFixture<AddUpdateAppealComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AddUpdateAppealComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(AddUpdateAppealComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
