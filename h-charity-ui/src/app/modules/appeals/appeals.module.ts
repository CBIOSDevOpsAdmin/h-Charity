import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppealsComponent } from './pages/appeals/appeals.component';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { AppealsRoutingModule } from './appeals-routing.module';
import { AppealViewComponent } from './pages/appeal-view/appeal-view.component';
import { AddUpdateAppealComponent } from './pages/add-update-appeal/add-update-appeal.component';

import { InputTextModule } from 'primeng/inputtext';
import { CheckboxModule } from 'primeng/checkbox';
import { EditorModule } from 'primeng/editor';
import { InputMaskModule } from 'primeng/inputmask';
import { InputSwitchModule } from 'primeng/inputswitch';
import { InputNumberModule } from 'primeng/inputnumber';
import { CalendarModule } from 'primeng/calendar';
import { ButtonModule } from 'primeng/button';
import { StepperModule } from 'primeng/stepper';

@NgModule({
  imports: [
    CommonModule,
    AppealsRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    FormsModule,
    InputTextModule,
    CheckboxModule,
    EditorModule,
    InputMaskModule,
    InputSwitchModule,
    InputNumberModule,
    CalendarModule,
    ButtonModule,
    StepperModule,
  ],
  declarations: [
    AppealsComponent,
    AppealViewComponent,
    AddUpdateAppealComponent,
  ],
})
export class AppealsModule {}
