import { IDropdown } from '../models/dropdown.model';

export function convertCountriesAndStatesObjectToDDObject(arrObject: any[]) {
  const response = arrObject.map((object: any) => {
    let obj: IDropdown = {
      name: object.name,
      value: object.iso2,
    };
    return obj;
  });
  return response;
}

export function convertCitiesObjectToDDObject(cities: any[]) {
  const response = cities.map((city: any) => {
    let obj: IDropdown = {
      name: city.name,
      value: city.name,
    };
    return obj;
  });
  return response;
}

export function convertArrStringToArrDDObject(arr: string[]) {
  let response: IDropdown[] = [];

  arr.forEach(element => {
    let obj: IDropdown = {
      name: element,
      value: element,
    };
    response.push(obj);
  });

  return response;
}

export function convertArrObjectToArrDDObject(
  arr: any[],
  nameProp: string,
  valueProp: string
) {
  let response: IDropdown[] = [];

  arr.forEach(element => {
    let obj: IDropdown = {
      name: element[nameProp],
      value: element[valueProp],
    };
    response.push(obj);
  });

  return response;
}

export function convertStringToDDObject(str: string) {
  const obj: IDropdown = {
    name: str,
    value: str,
  };

  return obj;
}
