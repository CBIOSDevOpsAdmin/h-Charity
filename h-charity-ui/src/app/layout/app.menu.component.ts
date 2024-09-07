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
  //#region Variables
  model: any[] = [];
  userRole: string = '';

  private initSubscription: Subscription;

  layoutService = inject(LayoutService);
  storageService = inject(StorageService);
  //#endregion

  ngOnInit() {
    this.initSubscription = this.storageService.initCalled$.subscribe(() => {
      this.loadMenuOptions();
    });

    this.model = [];
    this.loadMenuOptions();
  }

  //#region Private methods
  private loadMenuOptions() {
    this.loadUnauthenticatedMenuOptions();

    if (this.storageService.getUser()?.roles) {
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
            routerLink: ['/'],
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
    this.addAppealOptionToAllAuthenticatedUsers();
    switch (userRole) {
      case 'NORMAL_USER':
        break;
      case 'INSTITUTE_OWNER':
        break;

      case 'ORGANISATION_VOLUNTEER':
        this.model.find(x => {
          if (x.label === 'Institutions') {
            x.items.push({
              label: 'Add Institute',
              icon: 'pi pi-fw pi-file-edit',
              routerLink: ['/institutions/add'],
            });
          }
        });
        break;

      case 'ADMIN':
        this.model.find(x => {
          if (x.label === 'Institutions') {
            x.items.push({
              label: 'Add Institute',
              icon: 'pi pi-fw pi-file-edit',
              routerLink: ['/institutions/add'],
            });
          }
        });
        break;
    }
  }

  private addAppealOptionToAllAuthenticatedUsers() {
    this.model.find(x => {
      if (x.label === 'Appeals') {
        x.items.push({
          label: 'Add Appeal',
          icon: 'pi pi-fw pi-file-edit',
          routerLink: ['/appeals/add'],
        });
      }
    });
  }
  //#endregion
}
