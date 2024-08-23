export interface IImage {
  uuid?: string | null;
  uri: string;
  color?: IColor | null;
}

export interface IColor {
  uuid?: string | null;
  name: string;
}