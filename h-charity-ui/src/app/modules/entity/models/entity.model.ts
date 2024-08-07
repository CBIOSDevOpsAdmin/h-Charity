export interface IEntity {
  id?: number;
  name?: string;
  type?: string;
  president?: string;
  poc?: string;
  description?: string;
  isVerified?: boolean;
  hasInternet?: boolean;
  address?: IAddress;
  entityPhotos?: IPhotos;
  mobile?: string;
  office?: string;
  entityBankDetails?: IEntityBankDetails;
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
