import {
  ChangeDetectorRef,
  Component,
  Inject,
  Input,
  OnInit,
  PLATFORM_ID,
  ViewChild,
  inject,
} from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { EntityService } from '../../services/entity.service';
import {
  IAddress,
  IEntity,
  IEntityBankDetails,
} from '../../models/entity.model';
import { Galleria } from 'primeng/galleria';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IAppeal } from '../../models/appeal.model';
import { AppealService } from 'src/app/modules/appeals/services/appeal.service';
import { FeedbackService } from '../../services/feedback.service';
import { StorageService } from 'src/app/modules/shared/services/storage.service';
import { AppealsService } from '../../services/appeals.service';

@Component({
  selector: 'app-entity-view',
  templateUrl: './entity-view.component.html',
  styleUrl: './entity-view.component.css',
})
export class EntityViewComponent implements OnInit {
  //#region Variables
  @Input() appeal: any;
  @Input() appealForm: any;
  storageService = inject(StorageService);
  router = inject(Router);
  currentTime: string = '';
  dateString: string = '';
  entity: IEntity;
  address: string;
  bankDetails: IEntityBankDetails;
  images: string[] = [];

  route = inject(ActivatedRoute);
  entityService = inject(EntityService);
  storageService = inject(StorageService);

  arrImages: any[] | undefined;

  fullscreen: boolean = false;
  activeIndex: number = 0;
  onFullScreenListener: any;
  feedbackDialog: boolean = false;
  feedbackForm: FormGroup;
  statuses: any[] = [
    { label: 'Open', value: 'Open' },
    { label: 'Closed', value: 'Closed' },
  ];
  appeals: IAppeal[] = [];
  feedbacks: any[] = [];

  @ViewChild('galleria') galleria: Galleria | undefined;

  constructor(
    @Inject(PLATFORM_ID) private platformId: any,
    private cd: ChangeDetectorRef,
    private fb: FormBuilder,
    private feedbackService: FeedbackService,
    private appealsService: AppealsService
  ) {
    this.feedbackForm = this.fb.group({
      name: ['', Validators.required],
      contactNumber: ['', Validators.required],
      title: ['', Validators.required],
      description: ['', Validators.required],
      isAnonymous: [false],
      status: ['', Validators.required],
    });
  }

  responsiveOptions: any[] = [
    {
      breakpoint: '1024px',
      numVisible: 5,
    },
    {
      breakpoint: '768px',
      numVisible: 3,
    },
    {
      breakpoint: '560px',
      numVisible: 1,
    },
  ];
  //#endregion

  ngOnInit() {
    this.getEntity();
    this.bindDocumentListeners();
    this.loadFeedbacks();
    this.loadAppeals();
  }

  //#region Private Methods
  private getEntity() {
    this.entityService
      .getEntityById(this.route.snapshot.params['id'])
      .subscribe({
        next: (entity: IEntity) => {
          this.entity = entity;
          this.images = entity.entityPhotos.photos;
          this.prepareAddress(entity.address);
          this.bankDetails = entity.entityBankDetails;
          this.appeals = entity.appeals;
        },
      });
  }

  private prepareAddress(entityAddress: IAddress) {
    this.address =
      entityAddress.address1 +
      ', ' +
      entityAddress.address2 +
      ', ' +
      entityAddress.landmark +
      ', ' +
      entityAddress.city +
      ', ' +
      entityAddress.state +
      ', ' +
      entityAddress.country +
      '-' +
      entityAddress.pincode;
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
  //#endregion

  //#region Galleria
  toggleFullScreen() {
    if (this.fullscreen) {
      this.closePreviewFullScreen();
    } else {
      this.openPreviewFullScreen();
    }

    this.cd.detach();
  }

  openPreviewFullScreen() {
    let elem =
      this.galleria?.element.nativeElement.querySelector('.p-galleria');
    if (elem.requestFullscreen) {
      elem.requestFullscreen();
    } else if (elem['mozRequestFullScreen']) {
      /* Firefox */
      elem['mozRequestFullScreen']();
    } else if (elem['webkitRequestFullscreen']) {
      /* Chrome, Safari & Opera */
      elem['webkitRequestFullscreen']();
    } else if (elem['msRequestFullscreen']) {
      /* IE/Edge */
      elem['msRequestFullscreen']();
    }
  }

  onFullScreenChange() {
    this.fullscreen = !this.fullscreen;
    this.cd.detectChanges();
    this.cd.reattach();
  }

  closePreviewFullScreen() {
    if (document.exitFullscreen) {
      document.exitFullscreen();
    } else if (document['mozCancelFullScreen']) {
      document['mozCancelFullScreen']();
    } else if (document['webkitExitFullscreen']) {
      document['webkitExitFullscreen']();
    } else if (document['msExitFullscreen']) {
      document['msExitFullscreen']();
    }
  }

  bindDocumentListeners() {
    this.onFullScreenListener = this.onFullScreenChange.bind(this);
    document.addEventListener('fullscreenchange', this.onFullScreenListener);
    document.addEventListener('mozfullscreenchange', this.onFullScreenListener);
    document.addEventListener(
      'webkitfullscreenchange',
      this.onFullScreenListener
    );
    document.addEventListener('msfullscreenchange', this.onFullScreenListener);
  }

  unbindDocumentListeners() {
    document.removeEventListener('fullscreenchange', this.onFullScreenListener);
    document.removeEventListener(
      'mozfullscreenchange',
      this.onFullScreenListener
    );
    document.removeEventListener(
      'webkitfullscreenchange',
      this.onFullScreenListener
    );
    document.removeEventListener(
      'msfullscreenchange',
      this.onFullScreenListener
    );
    this.onFullScreenListener = null;
  }

  ngOnDestroy() {
    this.unbindDocumentListeners();
  }

  galleriaClass() {
    return `custom-galleria ${this.fullscreen ? 'fullscreen' : ''}`;
  }

  fullScreenIcon() {
    return `pi ${
      this.fullscreen ? 'pi-window-minimize' : 'pi-window-maximize'
    }`;
  }
  //#endregion

  //#region Feedback Form
  showFeedbackDialog() {
    this.feedbackDialog = true;
  }

  submitFeedback() {
    if (this.feedbackForm.valid) {
      // Handle form submission logic here
      this.feedbackDialog = false;
    }
  }

  loadFeedbacks(): void {
    this.feedbackService.getFeedbacks().subscribe(data => {
      this.feedbacks = data;
    });
  }

  loadAppeals(): void {
    this.appealsService.getAppeals().subscribe(data => {
      this.appeals = data;
    });
  }
  navigateToAddAppeal() {
    this.router.navigate(['appeals/add']);
  }
  //#endregion

  //#region Buttons
  public showEditButton(appeal: IAppeal): boolean {
    let roles = this.storageService.getUser().roles;

    if (
      appeal['user'] &&
      appeal['user'].id === this.storageService.getUser().id
    ) {
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

    if (
      appeal['user'] &&
      appeal['user'].id === this.storageService.getUser().id
    ) {
      return true;
    } else if (
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'))
    ) {
      return true;
    }

    return false;
  }

  //#endregion
}
