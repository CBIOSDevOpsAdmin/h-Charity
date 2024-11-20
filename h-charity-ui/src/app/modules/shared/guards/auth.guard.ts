import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router } from '@angular/router';
import { StorageService } from '../services/storage.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private storageService: StorageService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot): boolean {
    const user = this.storageService.getUser(); // Fetch user from StorageService
    const requiredRoles = route.data['roles'] as string[]; // Extract required roles

    if (user && user.roles && requiredRoles.includes(user.roles[0])) {
      return true;
    }

    this.router.navigate(['/notfound']);
    return false;
  }
}
