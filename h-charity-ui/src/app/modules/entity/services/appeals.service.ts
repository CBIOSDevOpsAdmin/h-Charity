import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppealsService {
  private apiUrl = 'assets/appeals.json'; // Path to your JSON file

  constructor(private http: HttpClient) { }

  getAppeals(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }
}
