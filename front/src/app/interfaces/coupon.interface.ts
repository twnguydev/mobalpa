export interface ICoupon {
  id?: number;
  code: string;
  name: string;
  discountRate: number;
  discountType: 'PERCENTAGE' | 'AMOUNT';
  dateStart: string;
  dateEnd: string;
  targetType: 'ALL_USERS' | 'USER' | 'SPECIFIC_USERS';
  targetUsers?: string[];
  maxUse: number;
  description?: string;
}
