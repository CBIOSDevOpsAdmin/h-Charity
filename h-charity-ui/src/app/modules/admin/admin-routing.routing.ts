import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { UsersComponent } from './pages/users/users.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: UsersComponent },
      { path: 'users', component: UsersComponent },
      // { path: 'add', component: AddUpdateAppealComponent },
      // { path: 'edit/:id', component: AddUpdateAppealComponent },
      // { path: 'view/:id', component: AppealViewComponent },
      { path: '**', redirectTo: '/notfound' },
    ]),
  ],
  exports: [RouterModule],
})
export class AdminRoutingModule {}
