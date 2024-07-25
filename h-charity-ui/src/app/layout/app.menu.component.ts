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
        label: 'Institutions',
        items: [
          {
            label: 'Institutions',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/institutions/'],
          },
          {
            label: 'Add Institution',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/institutions/add'],
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
            label: 'Add Appeal',
            icon: 'pi pi-fw pi-home',
            routerLink: ['/appeals/add'],
          },
        ],
      },
      {
        label: 'Pages',
        icon: 'pi pi-fw pi-briefcase',
        items: [
          {
            label: 'Crud',
            icon: 'pi pi-fw pi-pencil',
            routerLink: ['/pages/crud'],
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
