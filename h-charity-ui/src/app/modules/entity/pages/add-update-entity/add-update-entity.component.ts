import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { EntityService } from '../../services/entity.service';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';
import { WorldService } from 'src/app/modules/shared/services/world.service';
import {
  convertCountriesAndStatesObjectToDDObject,
  convertCitiesObjectToDDObject,
  convertArrStringToArrDDObject,
  convertStringToDDObject,
  convertArrObjectToArrDDObject,
} from 'src/app/modules/shared/utilities/common.utils';
import { DropdownChangeEvent } from 'primeng/dropdown';
import { IState } from 'src/app/modules/shared/models/state.model';
import { ICountry } from 'src/app/modules/shared/models/country.model';
import { ICity } from 'src/app/modules/shared/models/city.model';
import { IEntity } from '../../models/entity.model';
import { MessageService } from 'primeng/api';
import { UploadEvent } from 'primeng/fileupload';
import { FileService } from '../../services/file.service';
import { HttpResponse } from '@angular/common/http';
import { Observable, switchMap } from 'rxjs';
import { ActivatedRoute, Router } from '@angular/router';
import { StorageService } from 'src/app/modules/shared/services/storage.service';
import { UserService } from 'src/app/modules/shared/services/user.service';

@Component({
  selector: 'app-add-update-entity',
  templateUrl: './add-update-entity.component.html',
  styleUrls: ['./add-update-entity.component.css'],
})
export class AddUpdateEntityComponent implements OnInit {
  //#region Variables
  mode: string = 'Add';
  entityForm!: FormGroup;
  countries: IDropdown[];
  states: IDropdown[];
  cities: IDropdown[];
  entityTypes: IDropdown[] = [];
  entityOwnerOptions: IDropdown[] = [];
  uploadedFiles: any[] = [];
  message = '';
  fileInfos?: Observable<any>;
  entityId: number = 0;

  entity: IEntity;
  showReviewTab: boolean = false;
  showReviewForm: boolean = false;

  entityService = inject(EntityService);
  worldService = inject(WorldService);
  formBuilder = inject(FormBuilder);
  messageService = inject(MessageService);
  fileService = inject(FileService);
  route = inject(ActivatedRoute);
  router = inject(Router);
  storageService = inject(StorageService);
  userService = inject(UserService);
  //#endregion

  ngOnInit() {
    this.initDDFields();
    this.entityId = this.route.snapshot.params['id'];
    if (this.entityId && this.entityId > 0) {
      this.initFormNew();
      this.initFormEdit();
    } else {
      this.initFormNew();
    }

    this.entityService.refreshER$.subscribe(() => {
      this.initFormEdit();
    });
  }

  //#region Public Methods
  public saveEntity() {
    let payload = this.generatePayload(this.entityForm.value);
    // got error in the below line
    if (!this.validateEntityDetails()) {
      this.entityService.saveEntity(payload).subscribe({
        next: (response: IEntity) => {
          this.messageService.add({
            severity: 'success',
            summary: 'Save',
            detail: 'Entity saved successfully',
          });
          // this.router.navigateByUrl('/institutions');
          this.router.navigate(['institutions/edit', response]);
        },
      });
    }
  }

  onCountrySelect(e: DropdownChangeEvent) {
    this.worldService.getStates(e.value.value).subscribe({
      next: (states: IState[]) => {
        this.states = convertCountriesAndStatesObjectToDDObject(states);
      },
    });
  }

  onStateSelect(e: DropdownChangeEvent) {
    let ciso = this.entityForm.get(['address', 'country']).value.value;
    this.worldService.getCities(ciso, e.value.value).subscribe({
      next: (response: ICity[]) => {
        this.cities = convertCitiesObjectToDDObject(response);
      },
    });
  }

  onUpload(event: UploadEvent) {
    this.uploadedFiles = event['files'];
    this.uploadedFiles.forEach((file: any) => {
      this.uploadFileApiCall(file);
    });
  }

  onUploadCoverPhoto(event: UploadEvent) {
    this.fileService
      .onUploadCoverPhoto(this.entityId, event['files'][0])
      .subscribe({
        next: res => {
          if (res && res['status'] === 200) {
            this.messageService.add({
              severity: 'success',
              summary: 'Save',
              detail: 'Cover photo updated successfully',
            });
          }
        },
        error: error => {
          this.messageService.add({
            severity: 'error',
            summary: 'Error',
            detail: error['error'].message,
          });
        },
      });
  }
  //#endregion

  //#region Private Methods
  private validateEntityDetails() {
    let isError: boolean = false;

    return isError;
  }

  private initDDFields() {
    if (!(this.entityId > 0)) {
      this.worldService.getCountries().subscribe({
        next: (res: ICountry[]) => {
          this.countries = convertCountriesAndStatesObjectToDDObject(res);
        },
        error: error => {
          console.error(error);
        },
      });
    }

    this.entityTypes = convertArrStringToArrDDObject([
      'Mosque',
      'School',
      'Orphanage',
    ]);

    if (
      this.storageService.getUser()?.roles[0] === 'ORGANISATION_VOLUNTEER' ||
      this.storageService.getUser()?.roles[0] === 'ADMIN'
    ) {
      this.userService.getUsersByRole('INSTITUTE_OWNER').subscribe({
        next: (res: any[]) => {
          this.entityOwnerOptions = convertArrObjectToArrDDObject(
            res,
            'id',
            'username'
          );
        },
        error: error => {
          console.error(error);
        },
      });
    }
  }
  private updateLocationFields() {
    let country;
    let state;
    let city;
    let ciso: string = '';
    let siso: string = '';

    this.worldService
      .getCountries()
      .pipe(
        switchMap((countries: ICountry[]) => {
          this.countries = convertCountriesAndStatesObjectToDDObject(countries);
          country = this.countries.filter(
            x => x.name === this.entity.address.country
          )[0];
          ciso = country.value;
          return this.worldService.getStates(ciso);
        }),
        switchMap((states: IState[]) => {
          this.states = convertCountriesAndStatesObjectToDDObject(states);
          state = this.states.filter(
            x => x.name === this.entity.address.state
          )[0];
          siso = state.value;
          return this.worldService.getCities(ciso, siso);
        })
      )
      .subscribe({
        next: (response: ICity[]) => {
          this.cities = convertCitiesObjectToDDObject(response);
          city = this.cities.filter(
            x => x.name === this.entity.address.city
          )[0];

          this.entityForm.patchValue({
            address: {
              country: { ...country },
              state: { ...state },
              city: { ...city },
            },
          });
        },
        error: error => {
          console.error('Error occurred:', error);
        },
      });
  }

  private initFormNew() {
    this.entityForm = this.formBuilder.group({
      id: [0],
      name: ['', [Validators.required, Validators.pattern('^[a-zA-Z ]+$')]],
      type: ['', Validators.required],
      president: ['', Validators.required],
      poc: ['', Validators.required],
      description: ['', Validators.required],
      isVerified: [false],
      entityOwner: [0],
      hasInternet: [false],
      mobile: [
        '',
        [Validators.required, Validators.pattern(/\(\d{3}\) \d{3}-\d{4}/)],
      ],
      office: [''],
      address: this.formBuilder.group({
        address1: [''],
        address2: [''],
        landmark: [''],
        pincode: [''],
        city: [''],
        state: [''],
        country: [''],
      }),
    });

    this.entityForm.get('isVerified').disable();
  }

  private initFormEdit() {
    this.entityService.getEntityById(this.entityId).subscribe({
      next: (entity: IEntity) => {
        this.entity = entity;

        this.entityForm.patchValue({
          id: entity.id,
          name: entity.name,
          type: { ...convertStringToDDObject(entity.type) },
          president: entity.president,
          poc: entity.poc,
          description: entity.description,
          isVerified: entity.isVerified,
          entityOwner: entity.entityOwner['id'],
          hasInternet: entity.hasInternet,
          mobile: entity.mobile,
          office: entity.office,
          address: {
            address1: entity.address.address1,
            address2: entity.address.address2,
            landmark: entity.address.landmark,
            pincode: entity.address.pincode,
          },
        });
        this.updateLocationFields();
        this.entityForm.markAllAsTouched();

        this.initialiseShowReviewTab();
      },
    });
  }

  private generatePayload(data: any): IEntity {
    let payload = data;
    payload.type = payload.type.name;
    payload.address.country = payload.address.country.name;
    payload.address.state = payload.address.state.name;
    payload.address.city = payload.address.city.name;
    return payload;
  }

  private uploadFileApiCall(file: any) {
    this.fileService.upload(file, this.entityId).subscribe({
      next: (event: any) => {
        if (event instanceof HttpResponse) {
          this.message = event.body.message;
          this.fileInfos = this.fileService.getFiles();
        }

        this.messageService.add({
          severity: 'success',
          summary: 'Save',
          detail: 'File upload successfully',
        });
      },
      error: (err: any) => {
        if (err.error && err.error.message) {
          this.message = err.error.message;
        } else {
          this.message = 'Could not upload the file!';
        }
      },
      complete: () => {
        this.uploadedFiles = [];
      },
    });
  }

  private initialiseShowReviewTab() {
    const user = this.storageService.getUser();
    const role = user.roles[0];
    const isAdminOrOrganisationVolunteer = [
      'ADMIN',
      'ORGANISATION_VOLUNTEER',
    ].includes(role);
    const isEntityOwner = user.id === this.entity.entityOwner['id'];

    this.showReviewTab = isAdminOrOrganisationVolunteer || isEntityOwner;
    this.showReviewForm = isAdminOrOrganisationVolunteer;
  }
  //#endregion
}
