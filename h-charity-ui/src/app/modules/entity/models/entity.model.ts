import { IAppeal } from './appeal.model';

export interface IEntity {
  id?: number;
  name?: string;
  type?: string;
  president?: string;
  poc?: string;
  description?: string;
  isVerified?: boolean;
  entityOwner?: string;
  hasInternet?: boolean;
  address?: IAddress;
  entityPhotos?: IPhotos;
  mobile?: string;
  office?: string;
  entityBankDetails?: IEntityBankDetails;
  appeals?: IAppeal[];
  feedbacks?: IEntityFeedbackRes[];
}

export interface IAddress {
  id?: number;
  address1?: string; // Flat, House no., Building, Company, Apartment
  address2?: string; // Area, Street, Sector, Village
  landmark?: string;
  pincode?: string;
  city?: string;
  state?: string;
  country?: string;
}

export interface IPhotos {
  coverPhoto?: string;
  qrCode?: string;
  photos?: string[];
}

export interface IEntityBankDetails {
  id?: number;
  accountHolderName?: string;
  accountNo?: string;
  bankName?: string;
  branchName?: string;
  entityId?: number;
  ifscCode?: string;
  upiId?: string;
  upiNumber?: string;
}

export interface IEntityFeedback {
  id?: number;
  advisedBy?: string;
  advisedByContact?: string;
  title?: string;
  description?: string;
  isAnonymous?: boolean;
  status?: string;
}

export interface IEntityFeedbackRes {
  id?: number;
  advisedBy?: string;
  advisedByContact?: string;
  advisedDate?: Date;
  title?: string;
  description?: string;
  isAnonymous?: boolean;
  entityFeedbackStatusList?: IEntityFeedbackStatus;
}

export interface IEntityFeedbackStatus {
  id?: number;
  status?: string;
  statusComment?: string;
  statusCommentDate?: Date;
  statusCommenter?: string;
}
