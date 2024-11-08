import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class CommonService {
  http = inject(HttpClient);

  private resetAccessRights = new Subject<void>();
  resetAccessRights$ = this.resetAccessRights.asObservable();

  resetAccessRightsAcrossApp() {
    this.resetAccessRights.next();
  }
}
