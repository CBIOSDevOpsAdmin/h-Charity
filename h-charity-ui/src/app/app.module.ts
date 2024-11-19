import { NgModule } from '@angular/core';
import {
  CommonModule,
  HashLocationStrategy,
  LocationStrategy,
  PathLocationStrategy,
} from '@angular/common';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app-routing.module';
import { AppLayoutModule } from './layout/app.layout.module';
import { ProductService } from './demo/service/product.service';
import { CountryService } from './demo/service/country.service';
import { CustomerService } from './demo/service/customer.service';
import { EventService } from './demo/service/event.service';
import { IconService } from './demo/service/icon.service';
import { NodeService } from './demo/service/node.service';
import { PhotoService } from './demo/service/photo.service';
import { MessageService } from 'primeng/api';
import { httpInterceptorProviders } from './modules/shared/utilities/http.interceptor';
import { ToastModule } from 'primeng/toast';
import { NotfoundComponent } from './modules/shared/components/notfound/notfound.component';
import { MyProfileComponent } from './modules/shared/components/my-profile/my-profile.component';
import { DialogService } from 'primeng/dynamicdialog';
import { InputTextModule } from 'primeng/inputtext';
import { FormsModule } from '@angular/forms';
import { ProgressSpinnerModule } from 'primeng/progressspinner';
import { LoaderComponent } from './modules/shared/components/loader/loader.component';

@NgModule({
  declarations: [AppComponent, NotfoundComponent, MyProfileComponent, LoaderComponent,],
  imports: [
    FormsModule,
    AppRoutingModule,
    AppLayoutModule,
    ToastModule,
    InputTextModule,
    ProgressSpinnerModule,
    CommonModule,
    ProgressSpinnerModule,
  ],
  providers: [
    { provide: LocationStrategy, useClass: PathLocationStrategy },
    CountryService,
    CustomerService,
    EventService,
    IconService,
    NodeService,
    PhotoService,
    ProductService,
    MessageService,
    DialogService,
    httpInterceptorProviders,
  ],
  bootstrap: [AppComponent],
})
export class AppModule { }
