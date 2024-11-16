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
  IEntityFeedback,
  IEntityFeedbackRes,
} from '../../models/entity.model';
import { Galleria } from 'primeng/galleria';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { IAppeal } from '../../models/appeal.model';
import { FeedbackService } from '../../services/feedback.service';
import { StorageService } from 'src/app/modules/shared/services/storage.service';
import { convertArrStringToArrDDObject } from 'src/app/modules/shared/utilities/common.utils';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';
import { ConfirmationService, MessageService } from 'primeng/api';

@Component({
  selector: 'app-entity-view',
  templateUrl: './entity-view.component.html',
  styleUrl: './entity-view.component.css',
})
export class EntityViewComponent implements OnInit {
  //#region Variables
  @Input() appeal: any;
  @Input() appealForm: any;
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
  messageService = inject(MessageService);
  confirmationService = inject(ConfirmationService);

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
  isDialogVisible = false;
  feedback: any;
  showStatusChangeTable: boolean = false;

  @ViewChild('galleria') galleria: Galleria | undefined;

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

  showFeedbackButton: boolean = false;
  statusOptions: IDropdown[] = [];
  //#endregion

  constructor(
    @Inject(PLATFORM_ID) private readonly platformId: any,
    private readonly cd: ChangeDetectorRef,
    private readonly fb: FormBuilder,
    private readonly feedbackService: FeedbackService
  ) {
    this.statusOptions = convertArrStringToArrDDObject([
      'Open',
      'In Progress',
      'Completed',
      'ReOpen',
      'Closed Successful',
      'Closed Rejected',
    ]);

    this.initFeedbackForm();
  }

  ngOnInit() {
    this.getEntity();
    this.bindDocumentListeners();
    this.showFeedbackButton = !!this.storageService.getUser()?.id;
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
          this.feedbacks = entity.feedbacks;

          this.feedbacks.forEach(feedback => {
            feedback.entityFeedbackStatusList.sort((a, b) => {
              return (
                new Date(b.statusCommentDate).getTime() -
                new Date(a.statusCommentDate).getTime()
              );
            });
          });
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

  public showFeedbackEditButton(feedback: IEntityFeedbackRes): boolean {
    return this.canEditFeedback(feedback);
  }

  public showFeedbackDeleteButton(feedback: IEntityFeedbackRes): boolean {
    return this.canDeleteFeedback(feedback);
  }

  public canEditFeedback(feedback: IEntityFeedbackRes): boolean {
    const user = this.storageService.getUser();
    const roles = user.roles;
    const isInstituteOwner =
      this.entity && this.entity.entityOwner['id'] === user.id;
    const isAdminOrVolunteer =
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'));

    return isInstituteOwner || isAdminOrVolunteer;
  }

  public canDeleteFeedback(feedback: IEntityFeedbackRes): boolean {
    const user = this.storageService.getUser();
    const roles = user.roles;
    const isFeedbackOwner = feedback && feedback.advisedBy === user.username;
    const isAdminOrVolunteer =
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'));

    return isFeedbackOwner || isAdminOrVolunteer;
  }

  deleteFeedback(feedback: IEntityFeedbackRes) {
    this.confirmationService.confirm({
      target: event.target as EventTarget,
      message: 'Do you want to delete this Feedback?',
      header: 'Delete Confirmation',
      icon: 'pi pi-info-circle',
      acceptButtonStyleClass: 'p-button-danger p-button-text',
      rejectButtonStyleClass: 'p-button-text p-button-text',
      acceptIcon: 'none',
      rejectIcon: 'none',

      accept: () => {
        this.feedbackService.deleteFeedback(feedback.id).subscribe({
          next: () => {
            this.messageService.add({
              severity: 'success',
              summary: 'Deleted',
              detail: 'Feedback deleted successfully',
              life: 3000,
            });
            this.getEntity();
          },
        });
      },
    });
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
    return `pi ${this.fullscreen ? 'pi-window-minimize' : 'pi-window-maximize'
      }`;
  }
  //#endregion

  //#region Feedbacks
  showFeedbackDialog() {
    this.feedbackDialog = true;
  }

  submitFeedback() {
    if (this.feedbackForm.valid) {
      let payload = this.feedbackForm.getRawValue();
      payload.entityId = this.entity.id;
      this.feedbackService.saveFeedback(payload).subscribe({
        next: response => {
          this.messageService.add({
            severity: 'success',
            summary: 'Successful',
            detail: 'Feedback submitted successfully',
            life: 3000,
          });
          this.feedbackForm.reset();
          this.feedbackDialog = false;
          this.getEntity();
        },
      });
    }
  }

  loadFeedbacks(): void {
    // this.feedbackService.getFeedbacks().subscribe(data => {
    //   this.feedbacks = data;
    // });
  }

  loadAppeals(): void {
    // this.appealsService.getAppeals().subscribe(data => {
    //   this.appeals = data;
    // });
  }
  navigateToAddAppeal() {
    this.router.navigate(['appeals/add']);
  }

  private initFeedbackForm() {
    this.feedbackForm = this.fb.group({
      id: [0],
      advisedBy: [
        { value: this.storageService.getUser().username, disabled: true },
        Validators.required,
      ],
      advisedByContact: [
        { value: this.storageService.getUser().mobile, disabled: true },
        Validators.required,
      ],
      title: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(30)]],
      description: ['', Validators.required],
      isAnonymous: [false],
      status: [{ value: 'Open', disabled: true }],
    });
  }

  viewFeedback(feedback) {
    this.feedback = feedback;
    this.isDialogVisible = true;
  }

  canViewStatusChangeTable(): boolean {
    const user = this.storageService.getUser();
    const roles = user.roles;

    const isFeedbackOwner = this.feedback && this.feedback.advisedBy === user.username;
    const isInstituteOwner = this.entity && this.entity.entityOwner['id'] === user.id;
    const isAdminOrVolunteer =
      roles &&
      (roles.includes('ADMIN') || roles.includes('ORGANISATION_VOLUNTEER'));

    return isFeedbackOwner || isInstituteOwner || isAdminOrVolunteer;
  }

  // This method toggles the visibility of the status change table
  toggleStatusChangeTable(): void {
    if (this.canViewStatusChangeTable()) {
      this.showStatusChangeTable = !this.showStatusChangeTable;
    }
  }

  editFeedback(feedback: any) {
    this.feedback = { ...feedback };
    this.initFormEdit(this.feedback);
    this.feedbackDialog = true;
  }

  private initFormEdit(feedback: any) {
    this.feedbackForm.patchValue({
      id: feedback.id,
      advisedBy: feedback.advisedBy || this.storageService.getUser().username,
      advisedByContact: feedback.advisedByContact || this.storageService.getUser().mobile,
      title: feedback.title || '',
      description: feedback.description || '',
      isAnonymous: feedback.isAnonymous !== undefined ? feedback.isAnonymous : false,
      status: feedback.status || 'Open',
    });

    this.feedbackForm.get('status')?.enable();

  }


  //#endregion

  //#region Buttons

  //#endregion
}
