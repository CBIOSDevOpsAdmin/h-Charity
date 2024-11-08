import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HimanismRoutingModule } from './himanism-routing.module';

// Import PrimeNG components
import { CardModule } from 'primeng/card';
import { ButtonModule } from 'primeng/button';
import { InputTextModule } from 'primeng/inputtext';
import { AboutUsComponent } from './about-us/about-us.component';

@NgModule({
  imports: [
    CommonModule,
    HimanismRoutingModule,
    CardModule,
    ButtonModule,
    InputTextModule
  ],
  declarations: [
    AboutUsComponent,
  ],
  providers: [],
})
export class HimanismModule { }
