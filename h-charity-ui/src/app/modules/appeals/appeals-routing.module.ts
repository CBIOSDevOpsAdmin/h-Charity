import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AppealsComponent } from './pages/appeals/appeals.component';
import { AddUpdateAppealComponent } from './pages/add-update-appeal/add-update-appeal.component';
import { AppealViewComponent } from './pages/appeal-view/appeal-view.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: AppealsComponent },
      { path: 'add', component: AddUpdateAppealComponent },
      { path: 'edit/:id', component: AddUpdateAppealComponent },
      { path: 'view/:id', component: AppealViewComponent },
      { path: '**', redirectTo: '/notfound' },
    ]),
  ],
  exports: [RouterModule],
})
export class AppealsRoutingModule {}
