/* tslint:disable:no-unused-variable */
import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { By } from '@angular/platform-browser';
import { DebugElement } from '@angular/core';

import { AppealsComponent } from './appeals.component';

describe('AppealsComponent', () => {
  let component: AppealsComponent;
  let fixture: ComponentFixture<AppealsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AppealsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AppealsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
