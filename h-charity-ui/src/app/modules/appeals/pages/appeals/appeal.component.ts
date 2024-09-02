import { Component, OnInit } from '@angular/core';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-appeal',
  templateUrl: './appeal.component.html',
  styleUrl: './appeal.component.css',
})
export class AppealComponent implements OnInit {
  // loading: boolean = true;

  appealDialog: boolean = false;

  viewDialog: boolean = false;

  submitted: boolean = false;

  appeals: IAppeal[] = [];

  appeal: IAppeal;

  appealsForm: FormGroup;

  constructor(
    private appealsService: AppealService,
    private messageService: MessageService,
    private fb: FormBuilder,
    private router: Router
  ) { }

  ngOnInit() {
    this.appealsService.getAppeals().subscribe((data: any[]) => {
      this.appeals = data;
      // console.log(this.appeals);
    });

    this.appealsForm = this.fb.group({
      id: [0],
      title: ['', Validators.required],
      description: ['', Validators.required],
      onBehalfName: [''],
      requirementDate: [null],
      totalFundsRequired: [null],
      fundsReceived: [null],
      fundsNeeded: [null],
      zakatEligible: [''],
      interestEligible: [''],
      isAnonymous: [''],
      appealer: [{ value: '', disabled: true }],
      appealerMobile: [{ value: '', disabled: true }],
      verifier: [{ value: '', disabled: true }],
      verifierMobile: [{ value: '', disabled: true }],
      verifiedDate: [{ value: '', disabled: true }],
    });
  }

  editAppeal(appeal: IAppeal) {
    this.appealsForm.patchValue({
      id: appeal.id,
      title: appeal.title,
      description: appeal.description,
      onBehalfName: appeal.onBehalfName,
      requirementDate: new Date(
        formatDate(appeal.requirementDate, 'yyyy-MM-dd', 'en-US')
      ),
      totalFundsRequired: appeal.totalFundsRequired,
      fundsReceived: appeal.fundsReceived,
      fundsNeeded: appeal.fundsNeeded,
      zakatEligible: appeal.isZakatEligible,
      interestEligible: appeal.isInterestEligible,
      isAnonymous: appeal.isAnonymous,
      appealer: appeal.appealer,
      appealerMobile: appeal.appealerMobile,
      verifier: appeal.verifier,
      verifierMobile: appeal.verifierMobile,
      verifiedDate: appeal.verifiedDate,
    });
    this.appealDialog = true;
  }

  viewAppeal(appeal: IAppeal) {
    this.viewDialog = true;
    this.appeal = appeal;
  }

  hideDialog() {
    this.appealDialog = false;
    this.viewDialog = false;
    this.submitted = false;
  }

  public saveAppeal() {
    if (!this.validateAppealDetails()) {
      this.appealsService.saveAppeal(this.appealsForm.value).subscribe({
        next: response => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save',
            detail: 'Appeal saved successfully',
          });
          this.router.navigateByUrl('/appeals');
        },
      });
    }
  }

  private validateAppealDetails() {
    let isError: boolean = false;

    return isError;
  }
}