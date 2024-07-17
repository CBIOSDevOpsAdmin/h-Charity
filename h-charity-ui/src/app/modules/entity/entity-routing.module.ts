import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { EntityComponent } from './pages/entity/entity.component';
import { AddUpdateEntityComponent } from './components/add-update-entity/add-update-entity.component';
import { EntityViewComponent } from './components/entity-view/entity-view.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      // {
      //   path: 'list',
      //   data: { breadcrumb: 'List' },
      //   loadChildren: () =>
      //     import('./list/listdemo.module').then(m => m.ListDemoModule),
      // },
      // {
      //   path: 'media',
      //   data: { breadcrumb: 'Media' },
      //   loadChildren: () =>
      //     import('./media/mediademo.module').then(m => m.MediaDemoModule),
      // },
      // {
      //   path: 'table',
      //   data: { breadcrumb: 'Table' },
      //   loadChildren: () =>
      //     import('./table/tabledemo.module').then(m => m.TableDemoModule),
      // },
      { path: '', component: EntityComponent },
      { path: 'add', component: AddUpdateEntityComponent },
      { path: 'edit/:id', component: AddUpdateEntityComponent },
      { path: 'view/:id', component: EntityViewComponent },
      { path: '**', redirectTo: '/notfound' },
    ]),
  ],
  exports: [RouterModule],
})
export class EntityRoutingModule {}
