import { Component, inject, Input, OnInit } from '@angular/core';
import { IEntityReview } from '../../models/entity-review.model';
import { EntityService } from '../../services/entity.service';

@Component({
  selector: 'app-entity-review-table',
  templateUrl: './entity-review-table.component.html',
  styleUrls: ['./entity-review-table.component.css'],
})
export class EntityReviewTableComponent implements OnInit {
  @Input() entityId: number;
  reviews: IEntityReview;

  entityService = inject(EntityService);

  ngOnInit() {
    this.getReviewsByEntityId();

    this.entityService.refreshER$.subscribe(() => {
      this.getReviewsByEntityId();
    });
  }

  private getReviewsByEntityId() {
    this.entityService.getReviewsByEntityId(this.entityId).subscribe({
      next: reviews => {
        this.reviews = reviews;
      },
    });
  }
}
