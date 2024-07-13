import { Component, OnInit } from '@angular/core';
import { IAppeal } from '../../models/appeal.model';
import { ActivatedRoute } from '@angular/router';
import { EntityService } from '../../services/entity.service';
import { IEntity } from '../../models/entity.model';

@Component({
  selector: 'app-entity-view',
  templateUrl: './entity-view.component.html',
  styleUrl: './entity-view.component.css'
})
export class EntityViewComponent implements OnInit {

  // currentDateAndTime: Date;
  // private timer: any;

  currentTime: string = '';
  dateString: string = '';


  constructor(
    private route: ActivatedRoute,
    private entityService: EntityService,
  ) { }

  entity: IEntity;
  address: string;

  appeals: IAppeal[] = [
    {
      image: 'assets/mosque-details/madina-masjid1.png',
      title: 'Appeal 1',
      description: 'Namaz Carpet',
      status: 'Pending'
    },
    {
      image: 'assets/mosque-details/madina-masjid2.png',
      title: 'Appeal 2',
      description: 'Lights',
      status: 'Approved'
    },
    {
      image: 'assets/mosque-details/madina-masjid3.png',
      title: 'Appeal 3',
      description: 'Loud Speaker',
      status: 'Rejected'
    }
  ];

  images: string[] = [
    'assets/mosque-details/madina-masjid.png',
    'assets/mosque-details/madina-masjid2.png',
    'assets/mosque-details/madina-masjid1.png'
  ];


  committeeMembers = [
    { name: 'Abc', position: 'President' },
    { name: 'XYZ', position: 'Point Of Contact' },
  ];

  ngOnInit() {

    // this.updateDateTime();
    // // Update the time every second
    // this.timer = setInterval(() => {
    //   this.updateDateTime();
    // }, 1000);

    this.updateTime();
    setInterval(() => {
      this.updateTime();
    }, 1000);



    this.entityService.getEntityById(this.route.snapshot.params['id']).subscribe({
      next: (entity: IEntity) => {
        this.entity = entity;
        let eAddress = entity.address;
        this.address = eAddress.address1 + ", " + eAddress.address2 + "," + eAddress.landmark + "," + eAddress.city + "," + eAddress.state + "," + eAddress.country + "," + eAddress.pincode;
      }
    })
  }



  // ngOnDestroy(): void {
  //   // Clear the interval when the component is destroyed
  //   if (this.timer) {
  //     clearInterval(this.timer);
  //   }
  // }


  // private updateDateTime(): void {
  //   this.currentDateAndTime = new Date();
  // }

  updateTime(): void {
    const date = new Date();
    const hours = date.getHours().toString().padStart(2, '0');
    const minutes = date.getMinutes().toString().padStart(2, '0');
    const seconds = date.getSeconds().toString().padStart(2, '0');
    this.currentTime = `${hours}:${minutes}:${seconds}`;

    const day = date.toLocaleDateString('en-US', { weekday: 'long' });
    const month = date.toLocaleDateString('en-US', { month: 'long' });
    const dayOfMonth = date.getDate().toString();
    const year = date.getFullYear().toString();
    this.dateString = `${day}, ${dayOfMonth} ${month} ${year}`;
  }

}
