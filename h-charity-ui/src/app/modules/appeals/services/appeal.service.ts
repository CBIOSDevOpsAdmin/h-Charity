import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IAppeal } from '../models/appeal.model';

@Injectable({
  providedIn: 'root',
})
export class AppealService {
  rootURL = 'http://localhost:8080/api/v1/appeal';

  http = inject(HttpClient);

  saveAppeal(appeal: IAppeal) {
    return appeal.id > 0
      ? this.http.put(`${this.rootURL}`, appeal)
      : this.http.post(`${this.rootURL}`, appeal);
  }

  getAppealById(appealId: number): Observable<IAppeal> {
    return this.http.get(`${this.rootURL}/${appealId}`);
  }

  getAppeals(): Observable<IAppeal[]> {
    const url = `${this.rootURL}`;
    return this.http.get<IAppeal[]>(url);
  }

  getAllAppeals(): Observable<any> {
    return this.http.get<any>('assets/appeals.json');
  }

  getData(): Observable<any[]> {
    return this.http.get<any[]>('assets/appeals.json');
  }
}
