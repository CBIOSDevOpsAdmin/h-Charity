import { Inject, Injectable, inject } from '@angular/core';
import { HttpEvent, HttpInterceptor, HttpHandler, HttpRequest, HTTP_INTERCEPTORS, HttpErrorResponse } from '@angular/common/http';
import { Observable, catchError, switchMap, throwError } from 'rxjs';
import { AuthService } from '../../auth/services/auth.service';
import { EventData } from '../models/event.model';
import { EventBusService } from '../services/event-bus.service';
import { StorageService } from '../services/storage.service';

@Injectable()
export class HttpRequestInterceptor implements HttpInterceptor {
  private isRefreshing = false;
  isLoggedIn: boolean;

  constructor(
    private readonly authService: AuthService,
    private readonly storageService: StorageService,
    private readonly eventBusService: EventBusService
  ) {
    this.isLoggedIn = this.storageService.isLoggedIn();
  }

  intercept(
    req: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    if (!req.url.includes('api.countrystatecity')) {
      req = req.clone({
        withCredentials: true,
      });
    }

    return next.handle(req).pipe(
      catchError(error => {
        if (
          error instanceof HttpErrorResponse &&
          !req.url.includes('auth/signin') &&
          error.status === 401
        ) {
          return this.handle401Error(req, next);
        }

        return throwError(() => error);
      })
    );
  }

  private handle401Error(request: HttpRequest<any>, next: HttpHandler) {
    if (!this.isRefreshing) {
      this.isRefreshing = true;

      if (this.isLoggedIn) {
        return this.authService.refreshToken().pipe(
          switchMap(() => {
            this.isRefreshing = false;
            return next.handle(request);
          }),
          catchError(error => {
            this.isRefreshing = false;

            if (error.status == '403') {
              this.eventBusService.emit(new EventData('logout', null));
            }

            return throwError(() => error);
          })
        );
      }
    }

    return next.handle(request);
  }
}

export const httpInterceptorProviders = [
  { provide: HTTP_INTERCEPTORS, useClass: HttpRequestInterceptor, multi: true },
];
