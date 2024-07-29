export interface IUserData {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
  confirmPassword: string;
  birthdate: string;
  phone?: string | null;
  address?: string | null;
  city?: string | null;
  zipcode?: string | null;
  country?: string | null;
}