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
            {
              path: 'himanism',
              loadChildren: () =>
                import('./modules/himanism/himanism.module').then(
                  m => m.HimanismModule
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
export class AppRoutingModule {}
