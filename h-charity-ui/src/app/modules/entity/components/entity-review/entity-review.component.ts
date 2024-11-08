import { Component, inject, Input, OnInit } from '@angular/core';
import { IDropdown } from 'src/app/modules/shared/models/dropdown.model';
import { convertArrStringToArrDDObject } from 'src/app/modules/shared/utilities/common.utils';
import { EntityService } from '../../services/entity.service';
import { IEntityReview } from '../../models/entity-review.model';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-entity-review',
  templateUrl: './entity-review.component.html',
  styleUrls: ['./entity-review.component.css'],
})
export class EntityReviewComponent implements OnInit {
  @Input() entityId: number;
  reviewOptions: IDropdown[] = [];
  selectedReviewOption: any;
  comment: string;
  entityService = inject(EntityService);
  private readonly messageService = inject(MessageService);

  ngOnInit() {
    this.reviewOptions = convertArrStringToArrDDObject([
      'VERIFIED',
      'REJECTED',
      'NEEDS WORK',
    ]);
  }

  saveReview() {
    let entityReview: IEntityReview = {
      reviewStatus: this.selectedReviewOption,
      comment: this.comment,
      entityId: this.entityId,
    };
    this.entityService.saveReview(entityReview).subscribe({
      next: response => {
        this.messageService.add({
          severity: 'success',
          summary: 'Successful',
          detail: 'Save Successfully',
          life: 3000,
        });

        this.comment = '';
        this.selectedReviewOption = null;

        this.entityService.triggerERRefresh();
      },
      error(err) {
        console.error(err);
      },
    });
  }
}
