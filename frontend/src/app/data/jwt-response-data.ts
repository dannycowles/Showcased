export interface JwtResponseData {
  readonly token: string;
  readonly expiresIn: number;
}

export interface LoginResponseData extends JwtResponseData {
  readonly username: string;
  readonly profilePicture: string;
}
