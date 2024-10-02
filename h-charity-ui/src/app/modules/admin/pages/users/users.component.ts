import { Component, inject, OnInit } from '@angular/core';
import { MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { IUser } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
  //#region Variables
  userDialog: boolean = false;
  deleteUserDialog: boolean = false;
  deleteUsersDialog: boolean = false;
  users: IUser[] = [];
  user: IUser = {};
  selectedUsers: IUser[] = [];
  submitted: boolean = false;
  cols: any[] = [];
  roles: IDropdown[] = [];
  rowsPerPageOptions = [5, 10, 20];

  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);
  //#endregion

  ngOnInit() {
    // this.userService.getUsers().then(data => (this.users = data));

    this.cols = [
      { field: 'user', header: 'user' },
      { field: 'price', header: 'Price' },
      { field: 'category', header: 'Category' },
      { field: 'rating', header: 'Reviews' },
      { field: 'inventoryStatus', header: 'Status' },
    ];

    this.roles = [
      { name: 'Normal User', value: 'NORMAL_USER' },
      { name: 'Institute Owner', value: 'INSTITUTE_OWNER' },
      { name: 'Organisation Volunteer', value: 'ORGANISATION_VOLUNTEER' },
    ];
  }

  //#region Public Methods

  openNew() {
    this.user = {};
    this.submitted = false;
    this.userDialog = true;
  }

  deleteSelectedUsers() {
    this.deleteUsersDialog = true;
  }

  editUser(user: IUser) {
    this.user = { ...user };
    this.userDialog = true;
  }

  deleteUser(user: IUser) {
    this.deleteUserDialog = true;
    this.user = { ...user };
  }

  confirmDeleteSelected() {
    this.deleteUsersDialog = false;
    this.users = this.users.filter(val => !this.selectedUsers.includes(val));
    this.messageService.add({
      severity: 'success',
      summary: 'Successful',
      detail: 'Users Deleted',
      life: 3000,
    });
    this.selectedUsers = [];
  }

  confirmDelete() {
    this.deleteUserDialog = false;
    this.users = this.users.filter(val => val.id !== this.user.id);
    this.messageService.add({
      severity: 'success',
      summary: 'Successful',
      detail: 'User Deleted',
      life: 3000,
    });
    this.user = {};
  }

  hideDialog() {
    this.userDialog = false;
    this.submitted = false;
  }

  saveUser() {
    this.submitted = true;

    if (this.user.fullName?.trim()) {
      if (this.user.id) {
        // Update mode
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'User Updated',
          life: 3000,
        });
      } else {
        // Add mode
        this.user.id = 0;
        this.userService.saveUser(this.user).subscribe({
          next: (res: any) => {
            this.messageService.add({
              severity: 'success',
              summary: 'Successful',
              detail: 'User Created',
              life: 3000,
            });
          },
        });
      }

      this.users = [...this.users];
      this.userDialog = false;
      this.user = {};
    }
  }
  //#endregion

  //#region Private Methods
  findIndexById(id: string): number {
    let index = -1;
    for (let i = 0; i < this.users.length; i++) {
      // if (this.users[i].id === id) {
      //   index = i;
      //   break;
      // }
    }

    return index;
  }

  onGlobalFilter(table: Table, event: Event) {
    table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  }
  //#endregion
}
