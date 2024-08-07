import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';
import { AboutUsComponent } from './about-us/about-us.component';

@NgModule({
  imports: [
    RouterModule.forChild([
      { path: '', component: AboutUsComponent },
      { path: 'about-us', component: AboutUsComponent },
      { path: '**', redirectTo: '/notfound' },
    ]),
  ],
  exports: [RouterModule],
})
export class HimanismRoutingModule {}
