import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EntityComponent } from './pages/entity/entity.component';
import { AddUpdateEntityComponent } from './pages/add-update-entity/add-update-entity.component';
import { EntityRoutingModule } from './entity-routing.module';

import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { DataViewModule } from 'primeng/dataview';
import { PickListModule } from 'primeng/picklist';
import { OrderListModule } from 'primeng/orderlist';
import { InputTextModule } from 'primeng/inputtext';
import { DropdownModule } from 'primeng/dropdown';
import { RatingModule } from 'primeng/rating';
import { ButtonModule } from 'primeng/button';
import { EditorModule } from 'primeng/editor';
import { InputSwitchModule } from 'primeng/inputswitch';
import { CheckboxModule } from 'primeng/checkbox';
import { InputMaskModule } from 'primeng/inputmask';
import { PanelModule } from 'primeng/panel';
import { EntityService } from './services/entity.service';
import { FileUploadModule } from 'primeng/fileupload';
import { StripHtmlPipe } from '../shared/pipes/strip-html.pipe';
import { ToastModule } from 'primeng/toast';
import { TabViewModule } from 'primeng/tabview';
import { SplitterModule } from 'primeng/splitter';
import { CardModule } from 'primeng/card';
import { DividerModule } from 'primeng/divider';
import { TableModule } from 'primeng/table';
import { EntityPaymentAddComponent } from './components/entity-payment-add/entity-payment-add.component';
import { EntityBankDetailsComponent } from './components/entity-bank-details/entity-bank-details.component';
import { EntityViewComponent } from './pages/entity-view/entity-view.component';
import { SafeHtmlPipe } from '../shared/pipes/safe-html.pipe';
import { GalleriaModule } from 'primeng/galleria';
import { MessagesModule } from 'primeng/messages';
import { MessageModule } from 'primeng/message';
import { ScrollTopModule } from 'primeng/scrolltop';
import { CarouselModule } from 'primeng/carousel';
import { ImageModule } from 'primeng/image';
import { DialogModule } from 'primeng/dialog';
import { ConfirmDialogModule } from 'primeng/confirmdialog';
import { ConfirmationService } from 'primeng/api';

@NgModule({
  imports: [
    CommonModule,
    EntityRoutingModule,
    FormsModule,
    ReactiveFormsModule,
    FormsModule,
    DataViewModule,
    PickListModule,
    OrderListModule,
    InputTextModule,
    DropdownModule,
    RatingModule,
    ButtonModule,
    EditorModule,
    InputSwitchModule,
    CheckboxModule,
    InputMaskModule,
    PanelModule,
    FileUploadModule,
    ToastModule,
    TabViewModule,
    SplitterModule,
    CarouselModule,
    CardModule,
    TableModule,
    DividerModule,
    GalleriaModule,
    MessageModule,
    MessagesModule,
    ScrollTopModule,
    ImageModule,
    DialogModule,
    ConfirmDialogModule,
  ],
  declarations: [
    EntityComponent,
    AddUpdateEntityComponent,
    StripHtmlPipe,
    SafeHtmlPipe,
    EntityPaymentAddComponent,
    EntityBankDetailsComponent,
    EntityViewComponent,
  ],
  providers: [EntityService, ConfirmationService],
})
export class EntityModule {}
