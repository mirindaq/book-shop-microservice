import type { ResponseApi } from "./responseApi.type"

export interface RegisterRequest {
  username: string
  password: string
  email: string
  firstName?: string
  lastName?: string
}

export interface RegisterResponse {
  userId: string
  username: string
  email: string
  message: string
}

export type AuthRegisterResponse = ResponseApi<RegisterResponse>

