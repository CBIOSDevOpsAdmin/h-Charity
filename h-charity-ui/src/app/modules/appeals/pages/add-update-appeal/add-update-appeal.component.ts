import { Component, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';
import { InputSwitchChangeEvent } from 'primeng/inputswitch';

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
  formBuilder = inject(FormBuilder);
  messageService = inject(MessageService);
  appealService = inject(AppealService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  //#endregion

  ngOnInit() {
    this.appealId = this.route.snapshot.params['id'];
    if (this.appealId && this.appealId > 0) {
      this.initFormNew();
      // this.initFormEdit();
    } else {
      this.initFormNew();
    }
  }

  //#region Public Methods
  public saveAppeal() {
    let payload = this.appealForm.value;
    if (!this.validateAppealDetails()) {
      this.appealService.saveAppeal(payload).subscribe({
        next: (response: IAppeal) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save',
            detail: 'Appeal saved successfully',
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
    this.appealForm = this.formBuilder.group({
      id: [0],
      title: ['', [Validators.required]], // Mandatory validation
      description: ['', Validators.required], // Mandatory validation
      selfOrBehalf: [false],
      onBehalfName: [{ value: '', disabled: true }], // Enable validator conditionally
      totalFundsRequired: ['', [Validators.required]], // Mandatory validation
      fundsReceived: ['', [Validators.required]], // Adding required validator for completeness
      fundsNeeded: ['', [Validators.required]], // Adding required validator for completeness
      isZakatEligible: [false],
      isInterestEligible: [false],
      isAnonymous: [false],
      appealer: [{ value: '', disabled: true }, Validators.required],
      appealerMobile: [
        { value: '', disabled: true },
        [
          Validators.required,
          Validators.pattern("^((\\+91-?)|0)?[0-9]{10}$"),
        ]
      ],
      requirementDate: ['', [Validators.required, this.dateValidator]], // Mandatory validation and custom date validator
      verifier: [{ value: '', disabled: true }, Validators.required],
      verifierMobile: [
        { value: '', disabled: true },
        [
          Validators.required,
          Validators.pattern("^((\\+91-?)|0)?[0-9]{10}$"),
        ]
      ],
      verifiedDate: [{ value: '', disabled: true }, Validators.required],
    }, {
      validators: this.totalFundsValidator.bind(this) // Custom group validator for funds validation
    });

    this.appealForm.get('selfOrBehalf')?.valueChanges.subscribe((isOnBehalf) => {
      this.toggleOnBehalfFields(isOnBehalf);
    });
  }

  private toggleOnBehalfFields(isOnBehalf: boolean) {
    const onBehalfName = this.appealForm.get('onBehalfName');
    if (isOnBehalf) {
      onBehalfName?.enable();
      onBehalfName?.setValidators(Validators.required); // Conditional validator
    } else {
      onBehalfName?.disable();
      onBehalfName?.clearValidators(); // Clear validators when not on behalf
    }
    onBehalfName?.updateValueAndValidity();
  }

  private dateValidator(control: AbstractControl): { [key: string]: boolean } | null {
    const currentDate = new Date().setHours(0, 0, 0, 0);
    const selectedDate = new Date(control.value).setHours(0, 0, 0, 0);

    return selectedDate && selectedDate < currentDate ? { 'invalidDate': true } : null;
  }

  private totalFundsValidator(group: FormGroup): { [key: string]: boolean } | null {
    const totalFundsRequired = group.get('totalFundsRequired')?.value;
    const fundsReceived = group.get('fundsReceived')?.value;
    const fundsNeeded = group.get('fundsNeeded')?.value;

    return totalFundsRequired > (fundsReceived + fundsNeeded) ? { 'fundsMismatch': true } : null;
  }
  //#endregion
}