export interface ResponseApi<T> {
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