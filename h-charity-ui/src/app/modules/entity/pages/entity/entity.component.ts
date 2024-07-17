import { Component, OnInit, inject } from '@angular/core';
import { EntityService } from '../../services/entity.service';
import { SelectItem } from 'primeng/api';
import { IEntity } from '../../models/entity.model';
import { Router } from '@angular/router';
import { FileService } from '../../services/file.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-entity',
  templateUrl: './entity.component.html',
  styleUrls: ['./entity.component.scss'],
})
export class EntityComponent implements OnInit {
  entityService = inject(EntityService);
  fileService = inject(FileService);
  router = inject(Router);
  sortOptions: SelectItem[] = [];
  sortOrder: number = 0;
  sortField: string = '';
  entities: IEntity[] = [];
  imageInfos?: Observable<any>;

  ngOnInit() {
    this.entityService.getEntities().subscribe({
      next: entities => {
        this.entities = entities;
      },
    });

    // this.imageInfos = this.fileService.getFiles();

    this.sortOptions = [
      { label: 'Price High to Low', value: '!price' },
      { label: 'Price Low to High', value: 'price' },
    ];
  }

  onSortChange(event: any) {
    const value = event.value;

    if (value.indexOf('!') === 0) {
      this.sortOrder = -1;
      this.sortField = value.substring(1, value.length);
    } else {
      this.sortOrder = 1;
      this.sortField = value;
    }
  }

  onFilter(dv: DataView, event: Event) {
    // dv.filter((event.target as HTMLInputElement).value);
  }

  goToEntity(id: number) {
    this.router.navigate(['entities/edit', id]);
  }

  viewEntity(id: number) {
    this.router.navigate(['entities/view', id]);
  }
}
