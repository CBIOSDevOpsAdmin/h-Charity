import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';
import { NotfoundComponent } from './demo/components/notfound/notfound.component';
import { AppLayoutComponent } from './layout/app.layout.component';

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
              loadChildren: () =>
                import('./demo/components/dashboard/dashboard.module').then(
                  m => m.DashboardModule
                ),
            },
            {
              path: 'entities',
              loadChildren: () =>
                import('./modules/entity/entity.module').then(
                  m => m.EntityModule
                ),
            },
            {
              path: 'auth',
              loadChildren: () =>
                import('./modules/auth/auth.module').then(m => m.AuthModule),
            },
            {
              path: 'appeals',
              loadChildren: () =>
                import('./modules/appeals/appeals.module').then(
                  m => m.AppealsModule
                ),
            },
            {
              path: 'uikit',
              loadChildren: () =>
                import('./demo/components/uikit/uikit.module').then(
                  m => m.UIkitModule
                ),
            },
            {
              path: 'pages',
              loadChildren: () =>
                import('./demo/components/pages/pages.module').then(
                  m => m.PagesModule
                ),
            },
          ],
        },
        {
          path: 'auth',
          loadChildren: () =>
            import('./demo/components/auth/auth.module').then(
              m => m.AuthModule
            ),
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
