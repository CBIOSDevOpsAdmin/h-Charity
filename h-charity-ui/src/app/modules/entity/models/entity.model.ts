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
  photos?: string[];
  coverImage?: string;
  mobile?: string;
  office?: string;
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
