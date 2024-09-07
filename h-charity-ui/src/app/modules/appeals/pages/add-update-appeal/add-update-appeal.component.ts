import { Component, OnInit, inject } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
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
  minDate: Date;
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
      id: new FormControl(0),
      title: new FormControl('', Validators.required),
      description: new FormControl('', Validators.required),
      selfOrBehalf: new FormControl(false),
      onBehalfName: new FormControl({ value: '', disabled: true }, Validators.required),
      totalFundsRequired: new FormControl(null, Validators.required),
      fundsReceived: new FormControl(null, Validators.required),
      fundsNeeded: new FormControl(null, Validators.required),
      isZakatEligible: new FormControl(false),
      isInterestEligible: new FormControl(false),
      isAnonymous: new FormControl(false),
      appealer: new FormControl({ value: '', disabled: true }),
      appealerMobile: new FormControl({ value: '', disabled: true }),
      requirementDate: new FormControl(null, Validators.required),
      verifier: new FormControl({ value: '', disabled: true }),
      verifierMobile: new FormControl({ value: '', disabled: true }),
      verifiedDate: new FormControl({ value: '', disabled: true }),
    });

    this.minDate = new Date();

    // this.appealForm.get('selfOrBehalf')?.valueChanges.subscribe((isOnBehalf) => {
    //   this.toggleOnBehalfFields(isOnBehalf);
    // });
  }

  // private toggleOnBehalfFields(isOnBehalf: boolean) {
  //   const onBehalfName = this.appealForm.get('onBehalfName');
  //   if (isOnBehalf) {
  //     onBehalfName?.enable();
  //   } else {
  //     onBehalfName?.disable();
  //   }
  //   onBehalfName?.updateValueAndValidity();
  // }
  //#endregion
}