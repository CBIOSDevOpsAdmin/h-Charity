import { Component, inject, OnInit } from '@angular/core';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { ConfirmationService, MessageService } from 'primeng/api';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { DialogService, DynamicDialogRef } from 'primeng/dynamicdialog';
import { Router } from '@angular/router';
import { formatDate } from '@angular/common';
import { StorageService } from 'src/app/modules/shared/services/storage.service';

@Component({
  selector: 'app-appeal',
  templateUrl: './appeal.component.html',
  styleUrl: './appeal.component.css',
})
export class AppealComponent implements OnInit {
  // loading: boolean = true;
  storageService = inject(StorageService);

  appealDialog: boolean = false;

  viewDialog: boolean = false;

  submitted: boolean = false;

  appeals: IAppeal[] = [];

  appeal: IAppeal;

  appealsForm: FormGroup;

  minDate: Date;

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
      const today = new Date();
      this.minDate = today;
    });

    this.appealsForm = this.fb.group({
      id: [0],
      title: ['', Validators.required],
      description: ['', Validators.required],
      onBehalfName: [{ value: '', disabled: true }],
      requirementDate: [null, Validators.required],
      totalFundsRequired: [null, Validators.required],
      fundsReceived: [null, Validators.required],
      fundsNeeded: [null, Validators.required],
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

  editAppeal(id: number) {
    this.router.navigate(['appeals/edit', id]);
  }

  viewAppeal(appeal: IAppeal) {
    this.viewDialog = true;
    this.appeal = appeal;
  }

  hideDialog() {
    this.viewDialog = false;
    this.submitted = false;
  }

  public saveAppeal() {
    debugger;
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

  public showEditButton(appeal: IAppeal): boolean {
    let roles = this.storageService.getUser().roles;

    if (appeal['user'] && appeal['user'].id === this.storageService.getUser().id) {
      return true;
    } else if (
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'))
    ) {
      return true;
    }

    return false;
  }

  public showDeleteButton(appeal: IAppeal) {
    let roles = this.storageService.getUser().roles;

    if (appeal['user'] && appeal['user'].id === this.storageService.getUser().id) {
      return true;
    } else if (
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'))
    ) {
      return true;
    }

    return false;
  }
}
