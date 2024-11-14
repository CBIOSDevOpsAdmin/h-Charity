import { Component, inject, OnInit } from '@angular/core';
import { ConfirmationService, MessageService } from 'primeng/api';
import { Table } from 'primeng/table';
import { IUser } from '../../models/user.model';
import { UserService } from '../../services/user.service';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-users',
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css'],
})
export class UsersComponent implements OnInit {
  //#region Variables
  userForm: FormGroup;
  userDialog: boolean = false;
  deleteUserDialog: boolean = false;
  deleteUsersDialog: boolean = false;
  users: IUser[] = [];
  user: IUser = {};
  selectedUsers: IUser[] = [];
  submitted: boolean = false;
  roles: IDropdown[] = [];
  rowsPerPageOptions = [5, 10, 20];

  private readonly userService = inject(UserService);
  private readonly messageService = inject(MessageService);
  private readonly confirmationService = inject(ConfirmationService);
  private readonly fb: FormBuilder = inject(FormBuilder);
  //#endregion

  ngOnInit() {
    this.getUsers();
    this.initUserForm();

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
    let userIds = this.selectedUsers.map(user => user.id);
    this.userService.deleteUsers(userIds).subscribe({
      next: () => {
        this.users = this.users.filter(
          val => !this.selectedUsers?.includes(val)
        );

        this.deleteUsersDialog = false;
        this.selectedUsers = [];
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Users Deleted successfully',
          life: 3000,
        });
      },
    });
  }

  confirmDelete() {
    this.deleteUserDialog = false;

    this.userService.deleteUser(this.user.id).subscribe({
      next: () => {
        this.users = this.users.filter(val => val.id !== this.user.id);
        this.messageService.add({
          severity: 'success',
          summary: 'User Deleted',
          detail: 'User deleted successfully',
          life: 3000,
        });
        this.user = {};
      },
    });
  }

  hideDialog() {
    this.userDialog = false;
    this.submitted = false;
  }

  saveUser() {
    this.user = this.userForm.value;
    this.submitted = true;
    if (this.user.fullname?.trim()) {
      if (this.user.id) {
        this.userService.saveUser(this.user).subscribe({
          next: res => {
            this.getUsers();
            this.messageService.add({
              severity: 'success',
              summary: 'User updated successfully',
              detail: 'User Updated',
              life: 3000,
            });
          },
          error: err => {
            this.messageService.add({
              severity: 'error',
              summary: 'User update error',
              detail: err,
              life: 3000,
            });
          },
        });
      } else {
        // Add mode
        this.user.id = 0;
        this.userService.saveUser(this.user).subscribe({
          next: (res: any) => {
            this.messageService.add({
              severity: 'success',
              summary: 'User Created',
              detail: 'User created successfully',
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

  private getUsers() {
    this.userService.getUsers().subscribe({
      next: res => {
        this.users = res;
      },
    });
  }
  //#endregion

  //#region userForm
  private initUserForm() {
    this.userForm = this.fb.group({
      fullname: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
      username: ['', [Validators.required, Validators.pattern('^[a-zA-Z0-9_]+$'), Validators.minLength(6), Validators.maxLength(30)]],
      email: ['', [Validators.required, Validators.email]],
      mobile: [null, [Validators.required, Validators.pattern('^[0-9]{10}$')]],
      role: ['', [Validators.required]],
    });
  }

  transformUsername(event: Event) {
    const input = event.target as HTMLInputElement;
    const transformed = input.value.toLowerCase();
    this.userForm
      .get('username')
      ?.setValue(transformed, { emitEvent: false });
  }
  //#endregion
}
