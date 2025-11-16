export interface LoginRequest {
  username: string;
  password: string;
}

export interface LoginResponse {
  username: string;
}

export interface ErrorResponse {
  error: string;
}
