import { Component, inject, OnInit } from '@angular/core';
import { UserService } from '../../services/user.service';
import { IUser } from 'src/app/modules/admin/models/user.model';
import { DynamicDialogConfig } from 'primeng/dynamicdialog';

@Component({
  selector: 'app-my-profile',
  templateUrl: './my-profile.component.html',
  styleUrls: ['./my-profile.component.css'],
})
export class MyProfileComponent implements OnInit {
  //#region Variables
  config = inject(DynamicDialogConfig);
  user: IUser;
  //#endregion

  ngOnInit() {
    this.user = this.config.data.user;
  }
}
