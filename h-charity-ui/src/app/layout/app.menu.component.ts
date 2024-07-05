import { OnInit } from '@angular/core';
import { Component } from '@angular/core';
import { LayoutService } from './service/app.layout.service';

@Component({
  selector: 'app-menu',
  templateUrl: './app.menu.component.html',
})
export class AppMenuComponent implements OnInit {
  model: any[] = [];

  constructor(public layoutService: LayoutService) {}

  ngOnInit() {
    this.model = [
      {
        label: 'Home',
        items: [
          { label: 'Dashboard', icon: 'pi pi-fw pi-home', routerLink: ['/'] },
        ],
      },
      {
        label: 'Entities',
        items: [
          {
            label: 'Entities',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/entities/'],
          },
          {
            label: 'Add/Update Entity',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/entities/add-update'],
          },
        ],
      },
      {
        label: 'UI Components',
        items: [
          {
            label: 'Table',
            icon: 'pi pi-fw pi-table',
            routerLink: ['/uikit/table'],
          },
          {
            label: 'List',
            icon: 'pi pi-fw pi-list',
            routerLink: ['/uikit/list'],
          },

          {
            label: 'Media',
            icon: 'pi pi-fw pi-image',
            routerLink: ['/uikit/media'],
          },
        ],
      },
      {
        label: 'Pages',
        icon: 'pi pi-fw pi-briefcase',
        items: [
          {
            label: 'Auth',
            icon: 'pi pi-fw pi-user',
            items: [
              {
                label: 'Login',
                icon: 'pi pi-fw pi-sign-in',
                routerLink: ['/auth/login'],
              },
              {
                label: 'Error',
                icon: 'pi pi-fw pi-times-circle',
                routerLink: ['/auth/error'],
              },
              {
                label: 'Access Denied',
                icon: 'pi pi-fw pi-lock',
                routerLink: ['/auth/access'],
              },
            ],
          },
          {
            label: 'Crud',
            icon: 'pi pi-fw pi-pencil',
            routerLink: ['/pages/crud'],
          },
          {
            label: 'Not Found',
            icon: 'pi pi-fw pi-exclamation-circle',
            routerLink: ['/notfound'],
          },
          {
            label: 'Empty',
            icon: 'pi pi-fw pi-circle-off',
            routerLink: ['/pages/empty'],
          },
        ],
      },
      {
        label: 'HIMANISM',
        icon: 'pi pi-fw pi-briefcase',
        items: [
          {
            label: 'About Us',
            icon: 'pi pi-fw pi-pencil',
            routerLink: ['/himanism/about-us'],
          },
        ],
      },
    ];
  }
}
