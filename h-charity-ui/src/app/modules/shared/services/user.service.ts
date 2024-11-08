import { HttpClient, HttpParams } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../../admin/models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  rootURL = 'http://localhost:8080/api/v1/user';

  http = inject(HttpClient);

  getUserById(): Observable<IUser> {
    return this.http.get(`${this.rootURL}`);
  }

  getUsersByRole(role: string): Observable<any> {
    let params = new HttpParams().set('role', role);
    return this.http.get(`${this.rootURL}/byRole`, { params });
  }
}
