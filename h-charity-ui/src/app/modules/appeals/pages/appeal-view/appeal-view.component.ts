import { Component, Input, OnInit } from '@angular/core';
import { IAppeal } from '../../models/appeal.model';

@Component({
  selector: 'app-appeal-view',
  templateUrl: './appeal-view.component.html',
  styleUrls: ['./appeal-view.component.css']
})
export class AppealViewComponent implements OnInit {
  @Input() appeal: any;
  @Input() viewDialog: any;

  submitted: boolean = false;

  constructor() { }

  ngOnInit() {
  }

  viewAppeal(appeal: IAppeal) {
    this.viewDialog = true;
    this.appeal = appeal;
  }

  hideDialog() {
    this.viewDialog = false;
    this.submitted = false;
  }
}
