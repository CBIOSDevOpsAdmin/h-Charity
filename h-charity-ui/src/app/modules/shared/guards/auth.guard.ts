import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, Router, RouterStateSnapshot } from '@angular/router';
import { AuthService } from '../../auth/services/auth.service';

@Injectable({
  providedIn: 'root',
})
export class AuthGuard implements CanActivate {
  constructor(private authService: AuthService, private router: Router) { }

  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {


    if (!this.authService.isLoggedIn()) {
      this.router.navigate(['/auth/login']);
      return false;
    }

    // const requiredRoles: string[] = route.data['roles'];
    // const userRole: string = this.authService.getUserRole();

    // if (requiredRoles && !requiredRoles.includes(userRole)) {
    //   this.router.navigate(['/notfound']);
    //   return false;
    // }

    return true;
  }
}
