import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AddUpdateAppealComponent } from './components/add-update-appeal/add-update-appeal.component';
import { AppealsRoutingModule } from './appeals-routing.module';
import { FileUploadModule } from 'primeng/fileupload';
import { InputNumberModule } from 'primeng/inputnumber';
import { EditorModule } from 'primeng/editor';
import { ReactiveFormsModule } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
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
import { CalendarModule } from 'primeng/calendar';
import { CheckboxModule } from 'primeng/checkbox';
import { InputSwitchModule } from 'primeng/inputswitch';
import { DialogService, DynamicDialogModule, DynamicDialogRef } from 'primeng/dynamicdialog';





@NgModule({
  declarations: [
    AddUpdateAppealComponent,
    AppealComponent,

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
  ],

  providers: [AppealService, ConfirmationService, MessageService, DialogService],

})
export class AppealsModule { }
