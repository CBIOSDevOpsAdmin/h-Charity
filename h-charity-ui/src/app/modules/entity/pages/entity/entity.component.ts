import { Component, OnInit, inject } from '@angular/core';
import { EntityService } from '../../services/entity.service';
import { ConfirmationService, MessageService, SelectItem } from 'primeng/api';
import { IEntity } from '../../models/entity.model';
import { Router } from '@angular/router';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import { DataView } from 'primeng/dataview';
import { StorageService } from 'src/app/modules/shared/services/storage.service';
import { CommonService } from 'src/app/modules/shared/services/common.service';

@Component({
  selector: 'app-entity',
  templateUrl: './entity.component.html',
  styleUrls: ['./entity.component.scss'],
})
export class EntityComponent implements OnInit {
  //#region Variables
  viewOptions: SelectItem[] = [];
  sortOrder: number = 0;
  sortField: string = '';
  entities: IEntity[] = [];
  untouchedEntities: IEntity[] = [];
  imageInfos?: Observable<any>;

  entityService = inject(EntityService);
  commonService = inject(CommonService);
  fileService = inject(FileService);
  storageService = inject(StorageService);
  router = inject(Router);
  confirmationService = inject(ConfirmationService);
  messageService = inject(MessageService);

  showDeleteBtn: boolean = true;
  showEditBtn: boolean = true;
  userId: number = 0;
  roles: string;
  //#endregion

  ngOnInit() {
    this.getEntitiesApiCall();

    this.viewOptions = [
      { label: 'Show All', value: 'Show All' },
      { label: 'Show Verified', value: 'Show Verified' },
      { label: 'Show Unverified', value: 'Show Unverified' },
      { label: 'Show Mosques', value: 'Show Mosques' },
      { label: 'Show Schools', value: 'Show Schools' },
      { label: 'Show Orphanages', value: 'Show Orphanages' },
    ];
    this.commonService.resetAccessRights$.subscribe(() => {
      this.accessRights();
      this.isEditButtonDisabled(0);
    });

    this.accessRights();
  }

  //#region Public methods

  onViewChange(event: any) {
    const value = event.value;
    switch (value) {
      case 'Show Verified':
        this.entities = this.untouchedEntities.filter(
          (item: IEntity) => item.isVerified
        );
        break;
      case 'Show Unverified':
        this.entities = this.untouchedEntities.filter(
          (item: IEntity) => !item.isVerified
        );
        break;
      case 'Show Mosques':
        this.entities = this.untouchedEntities.filter((item: IEntity) =>
          item.type.toLowerCase().includes('mosque')
        );
        break;

      case 'Show Schools':
        this.entities = this.untouchedEntities.filter((item: IEntity) =>
          item.type.toLowerCase().includes('school')
        );
        break;

      case 'Show Orphanages':
        this.entities = this.untouchedEntities.filter((item: IEntity) =>
          item.type.toLowerCase().includes('orphanage')
        );
        break;

      default:
        this.entities = this.untouchedEntities;
        break;
    }
  }

  onFilter(dv: DataView, event: Event) {
    dv.filter((event.target as HTMLInputElement).value);
  }

  goToEntity(id: number) {
    this.router.navigate(['institutions/edit', id]);
  }

  viewEntity(id: number) {
    this.router.navigate(['institutions/view', id]);
  }

  deleteEntity(event: Event, id: number) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to delete this Institute?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-text',
      rejectButtonStyleClass: 'p-button-text p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',

      accept: () => {
        this.entityService.deleteEntity(id).subscribe({
          next: resp => {
            this.getEntitiesApiCall();
            this.messageService.add({
              severity: 'success',
              summary: 'Confirmed',
              detail: 'Institute deleted successfully',
            });
          },
        });
      },
    });
  }
  //#endregion

  //#region Private methods
  private getEntitiesApiCall() {
    this.entityService.getEntities().subscribe({
      next: entities => {
        this.entities = entities;
        this.untouchedEntities = entities;
      },
    });
  }

  private accessRights() {
    const user = this.storageService.getUser();
    this.roles = user?.roles || [];
    this.userId = user?.id || 0;
  }

  public isEditButtonDisabled(entityOwnerId: number): boolean {
    // Check if roles or userId are not available (indicating the user is not logged in)
    if (!this.roles || !this.userId) {
      return true; // Disable button if user is not logged in
    }

    // Allow if role is ADMIN or ORGANISATION_VOLUNTEER
    if (
      this.roles.includes('ADMIN') ||
      this.roles.includes('ORGANISATION_VOLUNTEER')
    ) {
      return false; // Don't disable button
    }

    // Allow if role is INSTITUTE_OWNER or NORMAL_USER and user ID matches entityOwnerId
    if (this.userId === entityOwnerId) {
      return false; // Don't disable button
    }

    // Disable button for all other cases
    return true;
  }
  //#endregion
}
