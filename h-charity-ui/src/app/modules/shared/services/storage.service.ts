import { Injectable, WritableSignal, signal } from '@angular/core';
import { Subject } from 'rxjs';

const USER_KEY = 'auth-user';

@Injectable({
  providedIn: 'root',
})
export class StorageService {
  loggedIn: WritableSignal<boolean> = signal(false);

  private initSource = new Subject<void>();
  initCalled$ = this.initSource.asObservable();

  callInit() {
    this.initSource.next();
  }

  clean(): void {
    window.sessionStorage.clear();
    this.loggedIn.set(false);
    this.initSource.next();
  }

  public saveUser(user: any): void {
    window.sessionStorage.removeItem(USER_KEY);
    window.sessionStorage.setItem(USER_KEY, JSON.stringify(user));
    this.loggedIn.set(true);
  }

  public getUser(): any {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      return JSON.parse(user);
    }

    return {};
  }

  public isLoggedIn(): boolean {
    const user = window.sessionStorage.getItem(USER_KEY);
    if (user) {
      this.loggedIn.set(true);
      return true;
    }

    return false;
  }
}
