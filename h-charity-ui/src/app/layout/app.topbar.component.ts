import { Component, ElementRef, OnInit, ViewChild } from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { Router } from '@angular/router';
import { User } from '../modules/auth/models/user.model';
import { AuthService } from '../modules/auth/services/auth.service';
import { EntityService } from '../modules/entity/services/entity.service';
import { StorageService } from '../modules/shared/services/storage.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
})
export class AppTopBarComponent implements OnInit {
  items!: MenuItem[];
  profileMenuItems!: MenuItem[];
  isLoggedIn = false;
  userDetails?: User;
  username: string = '';
  showProfileDialog: boolean = false;

  @ViewChild('menubutton') menuButton!: ElementRef;
  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;
  @ViewChild('topbarmenu') menu!: ElementRef;

  constructor(
    public layoutService: LayoutService,
    private messageService: MessageService,
    private router: Router,
    public storageService: StorageService,
    private authService: AuthService
  ) {
    this.items = [
      {
        label: 'Login',
        routerLink: ['/auth/login'],
      },
      { separator: true },
      { label: 'Register', routerLink: ['/auth/register'] },
    ];
  }
  ngOnInit(): void {
    this.isLoggedIn = this.storageService.isLoggedIn();

    this.profileMenuItems = [
      {
        label: 'Profile',
        command: () => {
          // this.getCallTest();
        },
      },
      { label: 'Settings' },
      {
        label: 'Logout',
        command: () => {
          this.logout();
        },
      },
    ];
  }

  logout() {
    this.authService.logout().subscribe({
      next: res => {
        this.storageService.clean();
        window.location.reload();
        this.router.navigate(['']);
      },
    });
  }

  save(severity: string) {
    this.messageService.add({
      severity: severity,
      summary: 'Success',
      detail: 'Data Saved',
    });
  }

  update() {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Data Updated',
    });
  }

  delete() {
    this.messageService.add({
      severity: 'success',
      summary: 'Success',
      detail: 'Data Deleted',
    });
  }

  login() {
    this.router.navigate(['/auth/login']);
  }
}
