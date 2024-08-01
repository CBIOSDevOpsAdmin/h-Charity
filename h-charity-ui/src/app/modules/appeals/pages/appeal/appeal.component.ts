import { Component, OnInit } from '@angular/core';
import { AppealService } from '../../services/appeal.service';
import { IAppeal } from '../../models/appeal.model';

@Component({
  selector: 'app-appeal',
  templateUrl: './appeal.component.html',
  styleUrl: './appeal.component.scss'
})
export class AppealComponent implements OnInit {

  // loading: boolean = true;

  // appeals: IAppeal[] = [
  //   {
  //     image: 'assets/mosque-details/madina-masjid1.png',
  //     title: 'Appeal 1',
  //     description: 'Namaz Carpet',
  //     status: 'Pending'
  //   },
  //   {
  //     image: 'assets/mosque-details/madina-masjid2.png',
  //     title: 'Appeal 2',
  //     description: 'Lights',
  //     status: 'Approved'
  //   },
  //   {
  //     image: 'assets/mosque-details/madina-masjid3.png',
  //     title: 'Appeal 3',
  //     description: 'Loud Speaker',
  //     status: 'Rejected'
  //   }
  // ];

  // constructor(private appealsService: AppealService) { }

  // ngOnInit() {
  //   this.appealsService.getData().subscribe((data: any[]) => {
  //     console.log(this.appeals);
  //     this.loading = false;
  //   });
  // }

  // clear(dt: any) {
  //   dt.clear();
  // }

  // onGlobalFilter(table: any, event: Event) {
  //   table.filterGlobal((event.target as HTMLInputElement).value, 'contains');
  // }

  appeals: IAppeal[] = [
    {
      title: 'Appeal 1',
      description: 'Namaz Carpet',
      status: 'Pending',
      category: '',

    },
    {
      title: 'Appeal 2',
      description: 'Lights',
      status: 'Approved',
      category: '',

    },
    {
      title: 'Appeal 3',
      description: 'Loud Speaker',
      status: 'Rejected',
      category: '',
    }
  ];

  constructor(private appealsService: AppealService) { }

  ngOnInit() {
    this.appealsService.getData().subscribe((data: any[]) => {
      console.log(this.appeals);
    });
  }



}
