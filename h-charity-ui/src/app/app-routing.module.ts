import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { AppLayoutComponent } from './layout/app.layout.component';
import { NotfoundComponent } from './modules/shared/components/notfound/notfound.component';
import { AuthGuard } from './modules/shared/guards/auth.guard';

@NgModule({
  imports: [
    RouterModule.forRoot(
      [
        {
          path: '',
          component: AppLayoutComponent,
          children: [
            {
              path: '',
              redirectTo: 'institutions',
              pathMatch: 'full',
            },
            {
              path: 'institutions',
              loadChildren: () =>
                import('./modules/entity/entity.module').then(
                  (m) => m.EntityModule
                ),
              canActivate: [AuthGuard],
              data: {
                roles: [
                  'NORMAL_USER',
                  'INSTITUTE_OWNER',
                  'ORGANISATION_VOLUNTEER',
                  'ADMIN',
                ],
              },
            },
            {
              path: 'appeals',
              loadChildren: () =>
                import('./modules/appeals/appeals.module').then(
                  (m) => m.AppealsModule
                ),
              canActivate: [AuthGuard],
              data: {
                roles: [
                  'NORMAL_USER',
                  'INSTITUTE_OWNER',
                  'ORGANISATION_VOLUNTEER',
                  'ADMIN',
                ],
              },
            },
            {
              path: 'auth',
              loadChildren: () =>
                import('./modules/auth/auth.module').then((m) => m.AuthModule),
            },
            {
              path: 'pages',
              loadChildren: () =>
                import('./demo/components/pages/pages.module').then(
                  (m) => m.PagesModule
                ),
            },
            {
              path: 'himanism',
              loadChildren: () =>
                import('./modules/himanism/himanism.module').then(
                  (m) => m.HimanismModule
                ),
            },
            {
              path: 'admin',
              loadChildren: () =>
                import('./modules/admin/admin.module').then(
                  (m) => m.AdminModule
                ),
              canActivate: [AuthGuard],
              data: { roles: ['ADMIN'] },
            },
          ],
        },
        { path: 'notfound', component: NotfoundComponent },
        { path: '**', redirectTo: '/notfound' },
      ],
      {
        scrollPositionRestoration: 'enabled',
        anchorScrolling: 'enabled',
        onSameUrlNavigation: 'reload',
      }
    ),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule { }
