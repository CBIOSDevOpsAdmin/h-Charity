import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IUser } from '../models/user.model';

@Injectable({
  providedIn: 'root',
})
export class UserService {
  rootURL = 'http://localhost:8080/api/v1/admin/user';

  http = inject(HttpClient);

  getUsers(): Observable<IUser[]> {
    return this.http.get<IUser[]>(`${this.rootURL}`);
  }

  saveUser(user: IUser) {
    return user.id > 0
      ? this.http.put(`${this.rootURL}`, user)
      : this.http.post(`${this.rootURL}`, user);
  }

  deleteUser(userId: number): Observable<any> {
    return this.http.delete(`${this.rootURL}/${userId}`);
  }
}
