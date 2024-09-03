import { IProduct, ICampaign } from './product.interface';

export interface INewsletterSend {
  subject: string;
  contentInParagraphStrings: string[];
  emails: string[];
  products: IProduct[];
  campaign: ICampaign;
  sendDate: string | null;
}

export interface INewsletter {
  id: string;
  email: string;
}