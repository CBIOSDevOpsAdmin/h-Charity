import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { IEntity, IEntityFeedback } from '../models/entity.model';

@Injectable({
  providedIn: 'root',
})
export class FeedbackService {
  rootURL = 'http://localhost:8080/api/v1/entity/feedback';

  http = inject(HttpClient);

  saveFeedback(feedback: IEntityFeedback) {
    return feedback.id > 0
      ? this.http.put(`${this.rootURL}`, feedback)
      : this.http.post(`${this.rootURL}`, feedback);
  }

  deleteFeedback(feedbackId: number): Observable<any> {
    return this.http.delete(`${this.rootURL}/${feedbackId}`);
  }
}
