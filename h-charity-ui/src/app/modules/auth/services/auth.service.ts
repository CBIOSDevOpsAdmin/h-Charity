import { HttpClient } from '@angular/common/http';
import { EventEmitter, Injectable } from '@angular/core';
import { Observable, tap } from 'rxjs';
import { ILoginResponse } from '../models/loginResponse.model';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  rootURL = 'http://localhost:8080/api/v1/auth';
  loggedIn = new EventEmitter<boolean>();

  constructor(private http: HttpClient) { }

  login(user: any): Observable<ILoginResponse> {
    return this.http.post<ILoginResponse>(`${this.rootURL}/signin`, user).pipe(
      tap((response) => {
        localStorage.setItem('token', response.token);
        localStorage.setItem('role', response.role); // Ensure role is stored
        this.loggedIn.emit(true);
      })
    );
  }

  register(user: any): Observable<any> {
    return this.http.post(`${this.rootURL}/signup`, user);
  }

  refreshToken(): Observable<any> {
    return this.http.post(`${this.rootURL}/refreshtoken`, {});
  }

  logout(): Observable<any> {
    return this.http.post(`${this.rootURL}/signout`, {}).pipe(
      tap(() => {
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        this.loggedIn.emit(false);
      })
    );
  }

  isLoggedIn(): boolean {
    const token = localStorage.getItem('token');
    return !!token;
  }

  getUserRole(): string {
    const role = localStorage.getItem('role');
    return role;
  }

}
