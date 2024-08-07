import { inject, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { StorageService } from '../modules/shared/services/storage.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-menu',
  templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
  model: any[] = [];
  userRole: string = '';

  private initSubscription: Subscription;

  layoutService = inject(LayoutService);
  storageService = inject(StorageService);

  ngOnInit() {
    this.initSubscription = this.storageService.initCalled$.subscribe(() => {
      this.loadMenuOptions();
    });

    this.model = [];
    this.loadMenuOptions();
  }

  public loadMenuOptions() {
    console.log('Apple');

    this.loadUnauthenticatedMenuOptions();

    if (this.storageService.getUser() && this.storageService.getUser().roles) {
      console.log('Auth');

      this.userRole = this.storageService.getUser().roles[0];
      this.loadAuthenticatedMenuOptions(this.userRole);
    }
  }

  private loadUnauthenticatedMenuOptions() {
    this.model = [
      {
        label: 'Institutions',
        items: [
          {
            label: 'Institutions',
            icon: 'pi pi-fw pi-warehouse',
            routerLink: ['/institutions/'],
          },

        ],
      },

      {
        label: 'Appeals',
        items: [
          {
            label: 'Appeals',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/appeals/'],
          },
          {
            label: 'Add/Update Appeals',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/appeals/add-update'],
          },

        ],
      },

      {
        label: 'Appeals',
        items: [
          {
            label: 'Appeals',
            icon: 'pi pi-fw pi-file',
            routerLink: ['/appeals/'],
          },
        ],
      },
      {
        label: 'HIMANISM',
        icon: 'pi pi-fw pi-briefcase',
        items: [
          {
            label: 'About Us',
            icon: 'pi pi-fw pi-id-card',
            routerLink: ['/himanism/about-us'],
          },
        ],
      },
    ];
  }

  private loadAuthenticatedMenuOptions(userRole: string) {
    switch (userRole) {
      case 'NORMAL_USER':
        console.log('NORMAL USER');

        this.model.find(x => {
          if (x.label === 'Institutions') {
            x.items.push({
              label: 'Add Appeal',
              icon: 'pi pi-fw pi-file-edit',
              routerLink: ['/appeals/add'],
            });
          }
        });
        break;
      case 'INSTITUTE_OWNER':
        break;

      case 'ORGANISATION_VOLUNTEER':
        break;

      case 'ADMIN':
        break;
    }
  }
}
