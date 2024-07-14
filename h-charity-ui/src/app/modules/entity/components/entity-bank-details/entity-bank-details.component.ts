import { Component, Input, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { EntityService } from '../../services/entity.service';
import { IEntityBankDetails } from '../../models/entity-bank-details.model';
import { MessageService } from 'primeng/api';

@Component({
  selector: 'app-entity-bank-details',
  templateUrl: './entity-bank-details.component.html',
  styleUrls: ['./entity-bank-details.component.css'],
})
export class EntityBankDetailsComponent implements OnInit {
  entityBankDetailsForm!: FormGroup;
  @Input() entityId: number;
  formBuilder = inject(FormBuilder);
  entityService = inject(EntityService);
  messageService = inject(MessageService);

  ngOnInit() {
    this.initFormNew();
    this.entityService.getEntityBankDetails(this.entityId).subscribe({
      next: entityBankDetails => {
        if (entityBankDetails) this.initEditForm(entityBankDetails);
      },
    });
  }

  private initEditForm(entityBankDetails: any) {
    this.entityBankDetailsForm.patchValue({
      id: entityBankDetails.id,
      bankName: entityBankDetails.bankName,
      accountHolderName: entityBankDetails.accountHolderName,
      branchName: entityBankDetails.branchName,
      accountNo: entityBankDetails.accountNo,
      ifscCode: entityBankDetails.ifscCode,
      upiId: entityBankDetails.upiId,
      upiNumber: entityBankDetails.upiNumber,
    });
  }

  savePaymentDetails() {
    let entityBankDetails: IEntityBankDetails =
      this.entityBankDetailsForm.value;
    entityBankDetails.entityId = this.entityId;
    console.log(entityBankDetails);
    this.entityService.saveEntityBankDetails(entityBankDetails).subscribe({
      next: data => {
        this.messageService.add({
          severity: 'success',
          summary: 'Save',
          detail: 'Entity Bank Details saved successfully',
        });
      },
    });
  }

  private initFormNew() {
    this.entityBankDetailsForm = this.formBuilder.group({
      id: [0],
      bankName: [''],
      accountHolderName: [''],
      branchName: [''],
      accountNo: [''],
      ifscCode: [''],
      upiId: [''],
      upiNumber: [''],
    });
  }

  onReset() {
    this.entityBankDetailsForm.reset();
  }
}
