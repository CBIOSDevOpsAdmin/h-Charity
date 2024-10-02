import { Component, OnInit } from '@angular/core';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
// import { TableDemoModule } from 'src/app/demo/components/uikit/table/tabledemo.module';
// import { TableDemoComponent } from 'src/app/demo/components/uikit/table/tabledemo.component';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';

@Component({
  selector: 'app-appeal',
  templateUrl: './appeal.component.html',
  styleUrl: './appeal.component.css',
})
export class AppealComponent implements OnInit {
  // loading: boolean = true;

  //#region Variables
  appealDialog: boolean = false;
  viewDialog: boolean = false;
  submitted: boolean = false;
  appeals: IAppeal[] = [];
  appeal: IAppeal;
  appealsForm: FormGroup;
  //#endregion

  constructor(
    private appealsService: AppealService,
    private messageService: MessageService,
    private confirmationService: ConfirmationService,
    private fb: FormBuilder,
    private router: Router
  ) {}

  ngOnInit() {
    this.getAppealsApiCall();

    this.appealsForm = this.fb.group({
      id: [0],
      title: ['', Validators.required],
      description: ['', Validators.required],
      onBehalfName: [''],
      requirementDate: [null],
      totalFundsRequired: [null],
      fundsRecived: [null],
      fundsNeeded: [null],
      zakatEligible: [''],
      interestEligible: [''],
      isAnonymous: [''],
      appealer: [''],
      appealerMobile: [''],
      verifier: [{ value: '', disabled: true }],
      verifierMobile: [{ value: '', disabled: true }],
      verifiedDate: [{ value: '', disabled: true }],
    });
  }
  //#region Public methods

  editAppeal(appeal: IAppeal) {
    this.appealsForm.patchValue({
      id: appeal.id,
      title: appeal.title,
      description: appeal.description,
      onBehalfName: appeal.onBehalfName,
      requirementDate: new Date(
        formatDate(appeal.requirementDate, 'yyyy-MM-dd', 'en-US')
      ),
      // requirementDate: new Date('2024-08-24'),
      fundsRequired: appeal.totalFundsRequired,
      fundsRecived: appeal.fundsReceived,
      fundsPending: appeal.fundsNeeded,
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

  deleteAppeal(appeal: IAppeal) {
    console.log(appeal);

    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to delete this Appeal?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-text',
      rejectButtonStyleClass: 'p-button-text p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',

      accept: () => {
        this.appealsService.deleteAppeal(appeal.id).subscribe({
          next: resp => {
            this.getAppealsApiCall();
            this.messageService.add({
              severity: 'success',
              summary: 'Appeal Delete',
              detail: 'Appeal deleted successfully',
            });
          },
        });
      },
      reject: () => {
        this.messageService.add({
          severity: 'error',
          summary: 'Rejected',
          detail: 'You have rejected',
        });
      },
    });
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

  //#endregion

  //#region Private methods
  private validateAppealDetails() {
    let isError: boolean = false;

    return isError;
  }

  private generatePayload(data: any): IAppeal {
    let payload = data;
    payload.type = payload.type.title;
    payload.type = payload.type.description;
    return payload;
  }

  private getAppealsApiCall() {
    this.appealsService.getAppeals().subscribe((data: any[]) => {
      this.appeals = data;
    });
  }

  //#endregion
}
