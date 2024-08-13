export interface IImage {
  uuid: string;
  uri: string;
  color: IColor | null;
}

export interface IColor {
  uuid: string;
  name: string;
}