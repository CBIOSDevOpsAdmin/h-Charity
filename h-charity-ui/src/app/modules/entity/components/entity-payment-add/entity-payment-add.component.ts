import { Component, Input, OnInit, inject } from '@angular/core';
import { UploadEvent } from 'primeng/fileupload';
import { FileService } from '../../services/file.service';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-entity-payment-add',
  templateUrl: './entity-payment-add.component.html',
  styleUrls: ['./entity-payment-add.component.css'],
})
export class EntityPaymentAddComponent implements OnInit {
  @Input() entity: number;

  fileService = inject(FileService);
  messageService = inject(MessageService);

  isFileUploaded = false;

  ngOnInit() { }

  onUpload(event: { files: File[] }) {
    const file = event.files[0];

    if (!file) {
      this.messageService.add({
        severity: 'error',
        summary: 'Error',
        detail: 'No file selected for upload.',
      });
      return;
    }

    this.fileService.uploadQRCode(this.entity, file).subscribe({
      next: (res) => {
        if (res && res['status'] === 200) {
          this.messageService.add({
            severity: 'success',
            summary: 'Success',
            detail: 'QR code uploaded successfully.',
          });

          this.isFileUploaded = true;
        }
      },
      error: (error) => {
        this.messageService.add({
          severity: 'error',
          summary: 'Error',
          detail: error?.error?.message || 'File upload failed.',
        });
      },
    });
  }

}
