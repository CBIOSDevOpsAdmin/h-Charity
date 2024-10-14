import { Component, OnInit, inject } from '@angular/core';
import {
  AbstractControl,
  FormBuilder,
  FormControl,
  FormGroup,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { InputSwitchChangeEvent } from 'primeng/inputswitch';
import { StorageService } from 'src/app/modules/shared/services/storage.service';
import { formatDate } from '@angular/common';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';
import { convertArrStringToArrDDObject } from 'src/app/modules/shared/utilities/common.utils';

@Component({
  selector: 'app-add-update-appeal',
  templateUrl: './add-update-appeal.component.html',
  styleUrls: ['./add-update-appeal.component.css'],
})
export class AddUpdateAppealComponent implements OnInit {
  //#region Variables
  mode: string = 'Add';
  appealForm!: FormGroup;
  appealId: number = 0;
  minDate: Date;
  appeal: IAppeal;
  formBuilder = inject(FormBuilder);
  messageService = inject(MessageService);
  appealService = inject(AppealService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  storageService = inject(StorageService);

  isVerifiedOptions: IDropdown[] = [];

  //#endregion

  ngOnInit() {
    this.appealId = this.route.snapshot.params['id'];
    if (this.appealId && this.appealId > 0) {
      this.initFormNew();
      this.initFormEdit();
    } else {
      this.initFormNew();
    }
  }

  //#region Public Methods
  public saveAppeal() {
    let payload = this.appealForm.getRawValue();
    if (!this.validateAppealDetails()) {
      this.appealService.saveAppeal(payload).subscribe({
        next: (response: IAppeal) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save',
            detail: 'Appeal saved successfully',
            key: 'toast1',
          });
          this.router.navigate(['appeals/edit', response.id]);
        },
      });
    }
  }

  onSelfOrBehalfChange(e: InputSwitchChangeEvent) {
    if (e.checked) {
      this.appealForm.get('onBehalfName')?.enable();
    }
  }

  //#endregion

  //#region Private Methods

  private validateAppealDetails() {
    let isError: boolean = false;

    return isError;
  }

  private initFormNew() {
    this.isVerifiedOptions = convertArrStringToArrDDObject([
      'Verified',
      'Unverified',
    ]);
    this.appealForm = this.formBuilder.group(
      {
        id: [0],
        title: ['', [Validators.required, Validators.minLength(10)]],
        description: ['', Validators.required],
        selfOrBehalf: [false],
        onBehalfName: [{ value: '', disabled: true }, Validators.required],
        isVerified: [{ value: 'Unverified', disabled: true }],
        totalFundsRequired: [null, [Validators.required]],
        fundsReceived: [null, [Validators.required]],
        fundsNeeded: [null, [Validators.required]],
        isZakatEligible: [false],
        isInterestEligible: [false],
        isAnonymous: [false],
        appealer: [
          { value: this.storageService.getUser().username, disabled: true },
          Validators.required,
        ],
        appealerMobile: [
          { value: this.storageService.getUser().mobile, disabled: true },
          [
            Validators.required,
            Validators.pattern('^((\\+91-?)|0)?[0-9]{10}$'),
          ],
        ],
        requirementDate: ['', [Validators.required]],
        verifier: [{ value: '', disabled: true }, Validators.required],
        verifierMobile: [
          { value: '', disabled: true },
          [
            Validators.required,
            Validators.pattern('^((\\+91-?)|0)?[0-9]{10}$'),
          ],
        ],
        verifiedDate: [{ value: '', disabled: true }, Validators.required],
      },
      { validators: this.fundsValidation }
    );
    this.minDate = new Date();
  }

  fundsValidation(control: AbstractControl) {
    const totalFundsRequired = control.get('totalFundsRequired')?.value;
    const fundsReceived = control.get('fundsReceived')?.value;
    const fundsNeeded = control.get('fundsNeeded')?.value;

    if (
      totalFundsRequired !== null &&
      fundsReceived !== null &&
      fundsNeeded !== null
    ) {
      const totalExpected = fundsReceived + fundsNeeded;

      if (totalFundsRequired < totalExpected) {
        control.get('totalFundsRequired')?.setErrors({ lessThanSum: true });
      } else {
        control.get('totalFundsRequired')?.setErrors(null);
      }
    }

    return null;
  }

  private initFormEdit() {
    this.appealService.getAppealById(this.appealId).subscribe({
      next: (appeal: IAppeal) => {
        this.appeal = appeal;
        this.appealForm.patchValue({
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

        this.appealForm.get('isVerified')?.enable();
      },
      error: error => {
        console.error('Error fetching appeal:', error);
      },
    });
  }

  //#endregion
}
