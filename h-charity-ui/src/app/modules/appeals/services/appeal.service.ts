import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IAppeal } from '../models/appeal.model';

@Injectable({
  providedIn: 'root'
})
export class AppealService {

  // private apiUrl = 'http://localhost:8080/api/v1/entity';

  private appealsUrl = 'assets/appeals.json';


  constructor(private http: HttpClient) { }

  // saveAppeal(formData: any): Observable<any> {
  //   return this.http.post(this.apiUrl, formData);
  // }

  getAllAppeals(): Observable<any> {
    return this.http.get<any>('assets/appeals.json');
  }

  saveAppeal(Appeal: IAppeal): Observable<any> {
    return this.http.get<any>('assets/appeals.json');
  }

  getData(): Observable<any[]> {
    return this.http.get<any[]>('assets/appeals.json');
  }
}
