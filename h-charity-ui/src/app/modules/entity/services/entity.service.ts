import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { IEntity, IEntityBankDetails } from '../models/entity.model';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class EntityService {
  rootURL = 'http://localhost:8080/api/v1/entity';

  http = inject(HttpClient);

  saveEntity(entity: IEntity) {
    return entity.id > 0
      ? this.http.put(`${this.rootURL}`, entity)
      : this.http.post(`${this.rootURL}`, entity);
  }

  getEntityById(entityId: number): Observable<IEntity> {
    return this.http.get(`${this.rootURL}/${entityId}`);
  }

  getEntities(): Observable<IEntity[]> {
    const url = `${this.rootURL}`;
    return this.http.get<IEntity[]>(url);
  }

  getEntity(): Observable<any> {
    return this.http.get(this.rootURL);
  }

  saveEntityBankDetails(entityBankDetails: IEntityBankDetails) {
    return this.http.post(`${this.rootURL}/bankDetails`, entityBankDetails);
  }

  updateEntityBankDetails(entityBankDetails: IEntityBankDetails) {
    return this.http.put(`${this.rootURL}/bankDetails`, entityBankDetails);
  }

  getEntityBankDetails(entityId: number) {
    return this.http.get(`${this.rootURL}/bankDetails/${entityId}`);
  }

  deleteEntity(entityId: number): Observable<any> {
    return this.http.delete(`${this.rootURL}/${entityId}`);
  }
}
