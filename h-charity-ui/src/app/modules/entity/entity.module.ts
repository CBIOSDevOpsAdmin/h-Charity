import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { EntityComponent } from './pages/entity/entity.component';
import { AddUpdateEntityComponent } from './components/add-update-entity/add-update-entity.component';
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
import { EntityPhotosAddComponent } from './components/entity-photos-add/entity-photos-add.component';
import { EntityPaymentAddComponent } from './components/entity-payment-add/entity-payment-add.component';
import { EntityPaymentViewComponent } from './components/entity-payment-view/entity-payment-view.component';
import { EntityAppealsComponent } from './components/entity-appeals/entity-appeals.component';
import { EntityBankDetailsComponent } from './components/entity-bank-details/entity-bank-details.component';
import { EntityViewComponent } from './components/entity-view/entity-view.component';
import { CarouselModule } from 'primeng/carousel';
import { CardModule } from 'primeng/card';
import { ImageModule } from 'primeng/image';
import { TableModule } from 'primeng/table';


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
    ImageModule,
    TableModule,



  ],
  declarations: [
    EntityComponent,
    AddUpdateEntityComponent,
    StripHtmlPipe,
    EntityPhotosAddComponent,
    EntityPaymentAddComponent,
    EntityPaymentViewComponent,
    EntityAppealsComponent,
    EntityBankDetailsComponent,
    EntityViewComponent
  ],
  providers: [EntityService],
})
export class EntityModule { }
