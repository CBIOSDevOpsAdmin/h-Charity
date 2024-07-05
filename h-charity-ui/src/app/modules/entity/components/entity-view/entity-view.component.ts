import { Component, OnInit } from '@angular/core';
import { IAppeal } from '../../models/appeal.model';

@Component({
  selector: 'app-entity-view',
  templateUrl: './entity-view.component.html',
  styleUrl: './entity-view.component.css'
})
export class EntityViewComponent implements OnInit {

  currentDateAndTime: Date;

  private timer: any;


  constructor() {
  }

  appeals: IAppeal[] = [
    {
      image: 'assets/mosque-details/madina-masjid1.png',
      title: 'Appeal 1',
      description: 'Description of Appeal 1',
      status: 'Pending'
    },
    {
      image: 'assets/mosque-details/madina-masjid2.png',
      title: 'Appeal 2',
      description: 'Description of Appeal 2',
      status: 'Approved'
    },
    {
      image: 'assets/mosque-details/madina-masjid3.png',
      title: 'Appeal 3',
      description: 'Description of Appeal 3',
      status: 'Rejected'
    }
  ];

  images: string[] = [
    'assets/mosque-details/madina-masjid.png',
    'assets/mosque-details/madina-masjid2.png',
    'assets/mosque-details/madina-masjid1.png'
  ];

  ngOnInit() {

    this.updateDateTime();
    // Update the time every second
    this.timer = setInterval(() => {
      this.updateDateTime();
    }, 1000);
  }

  ngOnDestroy(): void {
    // Clear the interval when the component is destroyed
    if (this.timer) {
      clearInterval(this.timer);
    }
  }

  private updateDateTime(): void {
    this.currentDateAndTime = new Date();
  }

}
