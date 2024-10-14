export interface IAppeal {
  id?: number;
  title?: string;
  description?: string;
  selfOrBehalf?: boolean;
  onBehalfName?: string;
  totalFundsRequired?: number;
  fundsReceived?: number;
  fundsNeeded?: number;
  isZakatEligible?: boolean;
  isInterestEligible?: boolean;
  isAnonymous?: boolean;
  appealer?: string;
  appealerMobile?: string;
  requirementDate?: string;
  verifier?: string;
  verifierMobile?: string;
  verifiedDate?: string;
  isVerified?: string;
  user?: any;
}
