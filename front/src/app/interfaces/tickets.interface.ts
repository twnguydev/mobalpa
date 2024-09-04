import { IUser } from "./user.interface";

export interface ITicket {
  uuid: string;
  user: IUser;
  subject: string;
  status: string;
  type: string;
  name: string;
  issue: string;
  createdAt: string;
  updatedAt: string;
  closedAt: string;
  responder: IUser;
  resolution: string;
}
