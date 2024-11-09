import {
  Component,
  ElementRef,
  inject,
  OnInit,
  ViewChild,
} from '@angular/core';
import { MenuItem, MessageService } from 'primeng/api';
import { LayoutService } from './service/app.layout.service';
import { Router } from '@angular/router';
import { User } from '../modules/auth/models/user.model';
import { AuthService } from '../modules/auth/services/auth.service';
import { StorageService } from '../modules/shared/services/storage.service';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { MyProfileComponent } from '../modules/shared/components/my-profile/my-profile.component';
import { UserService } from '../modules/shared/services/user.service';
import { IUser } from '../modules/admin/models/user.model';
import { CommonService } from '../modules/shared/services/common.service';

@Component({
  selector: 'app-topbar',
  templateUrl: './app.topbar.component.html',
})
export class AppTopBarComponent implements OnInit {
  //#region Variables
  items!: MenuItem[];
  profileMenuItems!: MenuItem[];
  isLoggedIn = false;
  userDetails?: User;
  username: string = '';
  showProfileDialog: boolean = false;

  @ViewChild('menubutton') menuButton!: ElementRef;
  @ViewChild('topbarmenubutton') topbarMenuButton!: ElementRef;
  @ViewChild('topbarmenu') menu!: ElementRef;

  ref: DynamicDialogRef | undefined;

  userService = inject(UserService);
  user: IUser;
  //#endregion

  constructor(
    public layoutService: LayoutService,
    private messageService: MessageService,
    private router: Router,
    public storageService: StorageService,
    private authService: AuthService,
    public dialogService: DialogService,
    private commonService: CommonService
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
          this.getUserDetails();
        },
      },
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
        this.commonService.resetAccessRightsAcrossApp();
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

  private getUserDetails() {
    this.userService.getUserById().subscribe({
      next: (user: IUser) => {
        this.ref = this.dialogService.open(MyProfileComponent, {
          header: 'My Profile',
          width: '30vw',
          modal: true,
          breakpoints: {
            '960px': '75vw',
            '640px': '90vw',
          },
          data: {
            user: user,
          },
        });
      },
      error: error => {
        console.error('Error fetching user:', error);
      },
    });
  }
}
