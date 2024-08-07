import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AppealService } from '../../services/appeal.service';
import { catchError, tap } from 'rxjs/operators';
import { of } from 'rxjs';
import { IAppeal } from '../../models/appeal.model';

@Component({
  selector: 'app-add-update-appeal',
  templateUrl: './add-update-appeal.component.html',
  styleUrl: './add-update-appeal.component.css'
})
export class AddUpdateAppealComponent implements OnInit {

  appealsForm: FormGroup;
  appealCategories: any[];
  appeals: any[] = [];
  fundControl = new FormControl();
  fundOptions = [
    { label: 'General Fund', value: 'general' },
    { label: 'Building Fund', value: 'building' },
    { label: 'Zakat Fund', value: 'zakat' },
    { label: 'Sadaqah Fund', value: 'sadaqah' },
    { label: 'Educational Fund', value: 'educational' },
    { label: 'Youth and Community Programs Fund', value: 'youth-community' },
    { label: 'Emergency Relief Fund', value: 'emergency-relief' },
    { label: 'Endowment (Waqf) Fund', value: 'endowment' }
  ];

  constructor(
    private fb: FormBuilder,
    private router: Router,
    private appealService: AppealService,
  ) { }

  ngOnInit(): void {
    this.appealsForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      costInvolve: [null, Validators.required],
      accumulatedAmount: [null, Validators.required],
      balanceAmount: [null, Validators.required],
      appealCategory: ['', Validators.required],
      owner: [{ value: 'Owner of the Appeal', disabled: true }],
      photos: [''],
      documents: [''],
      upiQRCode: [''],
      bankName: [''],
      branchname: [''],
      ifsc: [''],
      accountHolderName: [''],
      accountHolderNumber: [''],
      upiId: [''],
      upiNumber: ['']
    });

    this.appealCategories = [
      { label: 'General Fund', value: 'general-fund' },
      { label: 'Construction', value: 'construction' },
      { label: 'Maintenance', value: 'maintenance' },
      { label: 'Education Programs', value: 'education-programs' },
      { label: 'Charity Work', value: 'charity-work' }
    ];

    // Fetch all appeals
    this.appealService.getAllAppeals().subscribe({
      next: response => {
        this.appeals = response;
        console.log(this.appeals);
      }
    });
  }

  onFileSelect(event: any) {
    const file = event.files[0];

    if (file) {
      this.compressImage(file, 0.7, (compressedBlob: Blob) => {
        const compressedFile = new File([compressedBlob], file.name, {
          type: file.type
        });
        this.uploadFile(compressedFile);
      });
    }
  }

  compressImage(file: File, quality: number, callback: (blob: Blob) => void) {
    const reader = new FileReader();
    reader.readAsDataURL(file);

    reader.onload = (event: any) => {
      const img = new Image();
      img.src = event.target.result;

      img.onload = () => {
        const canvas = document.createElement('canvas');
        const ctx = canvas.getContext('2d') as CanvasRenderingContext2D;

        const maxWidth = 800;
        const scaleSize = maxWidth / img.width;
        canvas.width = maxWidth;
        canvas.height = img.height * scaleSize;

        ctx.drawImage(img, 0, 0, canvas.width, canvas.height);

        canvas.toBlob((blob) => {
          callback(blob as Blob);
        }, file.type, quality);
      };
    };
  }

  uploadFile(file: File) {
    console.log('Uploading file:', file);
  }

  onUpload(event: any) {
    for (const file of event.files) {
      console.log('File uploaded:', file);
    }
  }

  onSubmit() {
    console.log(this.appealsForm.value);
  }

  public saveAppeal() {
    let payload = this.generatePayload(this.appealsForm.value);
    this.appealService.saveAppeal(payload).subscribe({
      next: response => {
        this.router.navigateByUrl('/appeals');
      },
    });
  }

  // saveAppeal(generatePayload: any): void {
  //   this.appealService.saveAppeal(generatePayload).subscribe({
  //     next: (response) => {
  //       console.log('Appeal saved successfully:', response);
  //     },
  //     error: (error) => {
  //       console.error('Error saving appeal:', error);
  //     }
  //   });
  // }

  private generatePayload(data: any): IAppeal {
    let payload = data;
    payload.type = payload.type.item;
    payload.type = payload.type.costInvolve;
    payload.type = payload.type.accumulatedAmount;
    payload.type = payload.type.balanceAmount;
    payload.type = payload.type.bankName;
    payload.type = payload.type.branchname;
    payload.type = payload.type.accountHolderName;
    payload.type = payload.type.accountHolderNumber;
    payload.type = payload.type.upiId;
    payload.type = payload.type.upiNumber;
    return payload;
  }
}
