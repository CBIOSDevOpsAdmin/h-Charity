import { Component, OnInit } from '@angular/core';
import { LayoutService } from 'src/app/layout/service/app.layout.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { StorageService } from 'src/app/modules/shared/services/storage.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css'],
})
export class LoginComponent implements OnInit {
  checked: boolean = false;
  password!: string;
  username!: string;

  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';

  constructor(
    public layoutService: LayoutService,
    private authService: AuthService,
    private router: Router,
    private storageService: StorageService
  ) {
    if (this.storageService.isLoggedIn()) {
      this.isLoggedIn = true;
    }
  }

  ngOnInit() {}

  login() {
    let loginPayload = {
      username: this.username,
      password: this.password,
    };

    this.authService.login(loginPayload).subscribe({
      next: (data: any) => {
        this.storageService.saveUser(data);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.redirectToHome();
      },
      error: error => {
        console.error(error);
      },
    });
  }

  redirectToHome(): void {
    this.storageService.callInit();
    this.router.navigate(['']);
  }
}
