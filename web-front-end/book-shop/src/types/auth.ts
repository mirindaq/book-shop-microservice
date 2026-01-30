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

export interface ResponseSuccess<T> {
  status: number
  message: string
  data: T
}

export interface ResponseError {
  timestamp?: string
  status: number
  error: string
  path?: string
  message: string
}
