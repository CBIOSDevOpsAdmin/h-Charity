import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileUploadModule } from 'primeng/fileupload';
import { DropdownModule } from 'primeng/dropdown';
import { MultiSelectModule } from 'primeng/multiselect';
import { CardModule } from 'primeng/card';
import { TabViewModule } from 'primeng/tabview';
import { AppealService } from './services/appeal.service';
import { ToastModule } from 'primeng/toast';
import { AppealComponent } from './pages/appeal/appeal.component';
import { DataViewModule } from 'primeng/dataview';
import { TableModule } from 'primeng/table';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService, MessageService } from 'primeng/api';
import { ToggleButtonModule } from 'primeng/togglebutton';
import { DialogService, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';
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
  declarations: [
    AddUpdateAppealComponent,
    AppealComponent,
    AppealsComponent,
    AppealViewComponent,
    AddUpdateAppealComponent,
  ],
  imports: [
    CommonModule,
    AppealsRoutingModule,
    FileUploadModule,
    InputNumberModule,
    EditorModule,
    ReactiveFormsModule,
    InputTextModule,
    DropdownModule,
    MultiSelectModule,
    CardModule,
    TabViewModule,
    ToastModule,
    DataViewModule,
    TableModule,
    DialogModule,
    ConfirmDialogModule,
    ToggleButtonModule,
    CalendarModule,
    CheckboxModule,
    InputSwitchModule,
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

  providers: [AppealService, ConfirmationService, MessageService, DialogService],

})


export class AppealsModule { }
