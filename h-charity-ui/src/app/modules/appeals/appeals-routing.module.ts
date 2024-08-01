import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddUpdateAppealComponent } from './components/add-update-appeal/add-update-appeal.component';
import { AppealComponent } from './pages/appeal/appeal.component';

const routes: Routes = [
    {
        path: '',
        component: AddUpdateAppealComponent,
    }
];

@NgModule({
    imports: [
        RouterModule.forChild([
            { path: '', component: AppealComponent },
            { path: 'add-update', component: AddUpdateAppealComponent },
            { path: '**', redirectTo: '/notfound' },
        ]),
    ],
    exports: [RouterModule]
})
export class AppealsRoutingModule { }
