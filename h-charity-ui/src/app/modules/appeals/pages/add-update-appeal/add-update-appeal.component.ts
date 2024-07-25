import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { MessageService } from 'primeng/api';

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
  route = inject(ActivatedRoute);
  router = inject(Router);
  //#endregion

  ngOnInit() {
    // this.initDDFields();
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
    console.log(this.appealForm.value);

    //   let payload = this.generatePayload(this.entityForm.value);
    //   if (!this.validateEntityDetails()) {
    //     this.entityService.saveEntity(payload).subscribe({
    //       next: response => {
    //         this.messageService.add({
    //           severity: 'success',
    //           summary: 'Save',
    //           detail: 'Entity saved successfully',
    //         });
    //         this.router.navigateByUrl('/institutions');
    //       },
    //     });
    //   }
  }
  //#endregion

  //#region Private Methods

  // private validateEntityDetails() {
  //   let isError: boolean = false;

  //   return isError;
  // }
  // private initDDFields() {
  //   if (!(this.appealId > 0)) {
  //   }

  //   this.entityTypes = convertArrStringToArrDDObject([
  //     'Mosque',
  //     'School',
  //     'Orphanage',
  //   ]);
  // }

  private initFormNew() {
    this.appealForm = this.formBuilder.group({
      id: [0],
      title: [''],
      description: [''],
      selfOrBehalf: [false],
      onBehalfName: [{ value: '', disabled: true }, Validators.required],
      totalFundsRequired: [],
      fundsReceived: [],
      fundsNeeded: [],
      isZakatEligible: [false],
      isInterestEligible: [false],
      isAnonymous: [false],
      appealer: [''],
      appealerMobile: [''],
      requirementDate: [''],
      verifier: [{ value: '', disabled: true }, Validators.required],
      verifierMobile: [{ value: '', disabled: true }, Validators.required],
      verifiedDate: [{ value: '', disabled: true }, Validators.required],
    });
  }

  // private initFormEdit() {
  //   this.entityService.getEntityById(this.appealId).subscribe({
  //     next: (entity: IEntity) => {
  //       this.entity = entity;

  //       this.entityForm.patchValue({
  //         id: entity.id,
  //         name: entity.name,
  //         type: { ...convertStringToDDObject(entity.type) },
  //         president: entity.president,
  //         poc: entity.poc,
  //         description: entity.description,
  //         isVerified: entity.isVerified,
  //         hasInternet: entity.hasInternet,
  //         mobile: entity.mobile,
  //         office: entity.office,
  //         address: {
  //           address1: entity.address.address1,
  //           address2: entity.address.address2,
  //           landmark: entity.address.landmark,
  //           pincode: entity.address.pincode,
  //         },
  //       });
  //       this.updateLocationFields();
  //     },
  //   });
  // }

  // private generatePayload(data: any): IEntity {
  //   let payload = data;
  //   payload.type = payload.type.name;
  //   payload.address.country = payload.address.country.name;
  //   payload.address.state = payload.address.state.name;
  //   payload.address.city = payload.address.city.name;
  //   return payload;
  // }
  //#endregion
}
