import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { IEntity } from '../models/entity.model';
import { Observable } from 'rxjs';
import { IEntityBankDetails } from '../models/entity-bank-details.model';

@Injectable({
  providedIn: 'root',
})
export class EntityService {
  rootURL = 'http://localhost:8080/api/v1/entity';

  constructor(private http: HttpClient) {}

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

  saveEntityBankDetails(entityBankDetails: IEntityBankDetails) {
    return this.http.post(`${this.rootURL}/bankDetails`, entityBankDetails);
  }

  updateEntityBankDetails(entityBankDetails: IEntityBankDetails) {
    return this.http.put(`${this.rootURL}/bankDetails`, entityBankDetails);
  }

  getEntityBankDetails(entityId: number) {
    return this.http.get(`${this.rootURL}/bankDetails/${entityId}`);
  }

  // deleteParty(partyId: number): Observable<any> {
  //   return this.http.delete(`${this.baseUrl}/${partyId}`);
  // }
}
