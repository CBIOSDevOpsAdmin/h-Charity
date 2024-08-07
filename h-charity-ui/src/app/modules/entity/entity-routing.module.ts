import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EntityComponent } from './pages/entity/entity.component';
import { AddUpdateEntityComponent } from './pages/add-update-entity/add-update-entity.component';
import { EntityViewComponent } from './pages/entity-view/entity-view.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: EntityComponent },
      { path: 'add', component: AddUpdateEntityComponent },
      { path: 'edit/:id', component: AddUpdateEntityComponent },
      { path: 'view/:id', component: EntityViewComponent },
      { path: '**', redirectTo: '/notfound' },
    ]),
  ],
  exports: [RouterModule],
})
export class EntityRoutingModule { }
