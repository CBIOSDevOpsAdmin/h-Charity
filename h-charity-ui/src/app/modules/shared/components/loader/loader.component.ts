// loader.component.ts
import { Component, OnInit, inject } from '@angular/core';
import { LoaderService } from '../../services/loader.service';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-loader',
  templateUrl: './loader.component.html',
  styleUrls: ['./loader.component.css'],
})
export class LoaderComponent implements OnInit {
  isLoading: Observable<boolean>;

  private loaderService = inject(LoaderService);

  constructor() {
    this.isLoading = this.loaderService.loading$;
  }

  ngOnInit(): void { }
}
