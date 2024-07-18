import { Component, OnInit, inject } from '@angular/core';
import { EntityService } from '../../services/entity.service';
import { SelectItem } from 'primeng/api';
import { IEntity } from '../../models/entity.model';
import { Router } from '@angular/router';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';
import { DataView } from 'primeng/dataview';

@Component({
  selector: 'app-entity',
  templateUrl: './entity.component.html',
  styleUrls: ['./entity.component.scss'],
})
export class EntityComponent implements OnInit {
  entityService = inject(EntityService);
  fileService = inject(FileService);
  router = inject(Router);
  viewOptions: SelectItem[] = [];
  sortOrder: number = 0;
  sortField: string = '';
  entities: IEntity[] = [];
  untouchedEntities: IEntity[] = [];
  imageInfos?: Observable<any>;

  ngOnInit() {
    this.entityService.getEntities().subscribe({
      next: entities => {
        this.entities = entities;
        this.untouchedEntities = entities;
      },
    });

    this.viewOptions = [
      { label: 'Show All', value: 'Show All' },
      { label: 'Show Verified', value: 'Show Verified' },
      { label: 'Show Unverified', value: 'Show Unverified' },
      { label: 'Show Mosques', value: 'Show Mosques' },
      { label: 'Show Schools', value: 'Show Schools' },
      { label: 'Show Orphanages', value: 'Show Orphanages' },
    ];
  }

  onViewChange(event: any) {
    const value = event.value;
    debugger;
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
    this.router.navigate(['entities/edit', id]);
  }

  viewEntity(id: number) {
    this.router.navigate(['entities/view', id]);
  }
}
