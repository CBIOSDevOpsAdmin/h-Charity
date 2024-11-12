import { Component, inject, OnInit } from '@angular/core';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { ConfirmationService, MessageService, SelectItem } from 'primeng/api';
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
  appealDialog: boolean = false;
  viewDialog: boolean = false;
  submitted: boolean = false;
  appeals: IAppeal[] = [];
  appeal: IAppeal;
  appealsForm: FormGroup;
  minDate: Date;
  yesNoOptions = [
    { label: 'Yes', value: true },
    { label: 'No', value: false },
  ];

  storageService = inject(StorageService);
  appealsService = inject(AppealService);
  messageService = inject(MessageService);
  fb = inject(FormBuilder);
  router = inject(Router);
  confirmationService = inject(ConfirmationService);

  ngOnInit() {
    this.getAppeals();

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

  public canEditOrDelete(appeal: IAppeal): boolean {
    const user = this.storageService.getUser();
    const roles = user.roles;
    const isOwner = appeal['user'] && appeal['user'].id === user.id;
    const isAdminOrVolunteer =
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'));

    return isOwner || isAdminOrVolunteer;
  }

  public showEditButton(appeal: IAppeal): boolean {
    return this.canEditOrDelete(appeal);
  }

  public showDeleteButton(appeal: IAppeal): boolean {
    return this.canEditOrDelete(appeal);
  }

  deleteAppeal(appeal: IAppeal) {
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
          next: response => {
            this.getAppeals();
            this.messageService.add({
              severity: 'success',
              summary: 'Delete',
              detail: 'Appeal deleted successfully',
            });
          },
        });
      },
    });
  }

  private getAppeals() {
    this.appealsService.getAppeals().subscribe((data: any[]) => {
      this.appeals = data;
      const today = new Date();
      this.minDate = today;
    });
  }
}
