import { inject, OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';
import { StorageService } from '../modules/shared/services/storage.service';
import { Subscription } from 'rxjs';
import { EntityService } from '../modules/entity/services/entity.service';
import { MenuItem } from 'primeng/api';

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
  entityService = inject(EntityService);

  //#endregion

  ngOnInit() {
    this.initSubscription = this.storageService.initCalled$.subscribe(() => {
      this.loadMenuOptions();
      this.checkIfInstituteOwnerExists();
    });

    this.model = [];
    this.loadMenuOptions();

    this.checkIfInstituteOwnerExists();
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
        // this.model.find(x => {
        //   if (x.label === 'Institutions') {
        //     x.items.push({
        //       label: 'Add Institute',
        //       icon: 'pi pi-fw pi-file-edit',
        //       routerLink: ['/institutions/add'],
        //     });
        //   }
        // });
        break;

      case 'ADMIN':
        // this.model.find(x => {
        //   if (x.label === 'Institutions') {
        //     x.items.push({
        //       label: 'Add Institute',
        //       icon: 'pi pi-fw pi-file-edit',
        //       routerLink: ['/institutions/add'],
        //     });
        //   }
        // });

        this.model.push({
          label: 'Admin',
          items: [
            {
              label: 'Users',
              icon: 'pi pi-fw pi-users',
              routerLink: ['/admin/users'],
            },
          ],
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

  // private checkIfInstituteOwnerExists() {
  //   if (this.storageService.getUser().id) {
  //     this.entityService
  //       .checkIfInstituteOwnerExists(this.storageService.getUser().id)
  //       .subscribe({
  //         next: (response: any) => {
  //           if (response) {
  //             this.model.find(x => {
  //               if (x.label === 'Institutions') {
  //                 x.items = x.items.filter(
  //                   (item: { label: string }) => item.label !== 'Add Institute'
  //                 );
  //               }
  //             });
  //           } else {
  //             this.model.find(x => {
  //               if (x.label === 'Institutions') {
  //                 x.items.push({
  //                   label: 'Add Institute',
  //                   icon: 'pi pi-fw pi-file-edit',
  //                   routerLink: ['/institutions/add'],
  //                 });
  //               }
  //             });
  //           }
  //         },
  //       });
  //   }
  // }

  private checkIfInstituteOwnerExists() {
    const userId = this.storageService.getUser()?.id;

    if (!userId) return;

    this.entityService.checkIfInstituteOwnerExists(userId).subscribe({
      next: (response: boolean) => {
        const institutionMenu = this.model.find(x => x.label === 'Institutions');

        if (!institutionMenu || !institutionMenu.items) return;

        if (response) {
          institutionMenu.items = institutionMenu.items.filter(
            (item: MenuItem) => item.label !== 'Add Institute'
          );
        } else {
          const addInstituteExists = institutionMenu.items.some(
            (item: MenuItem) => item.label === 'Add Institute'
          );

          if (!addInstituteExists) {
            institutionMenu.items.push({
              label: 'Add Institute',
              icon: 'pi pi-fw pi-file-edit',
              routerLink: ['/institutions/add'],
            });
          }
        }
      },
      error: (err) => {
        console.error('Error checking institute owner:', err);
      }
    });
  }


  //#endregion
}
