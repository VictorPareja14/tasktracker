export interface UserResponse {
  id: number;
  username: string;
  email: string;
  role: string; // "ADMIN" | "USER"
}