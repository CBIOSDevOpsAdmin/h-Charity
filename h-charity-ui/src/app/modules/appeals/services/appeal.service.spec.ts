/* tslint:disable:no-unused-variable */

import { TestBed, async, inject } from '@angular/core/testing';
import { AppealService } from './appeal.service';

describe('Service: Appeal', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [AppealService]
    });
  });

  it('should ...', inject([AppealService], (service: AppealService) => {
    expect(service).toBeTruthy();
  }));
});
