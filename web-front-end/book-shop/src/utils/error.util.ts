import axios from "axios"
import type { ResponseError } from "@/types/auth.type"

/** Lấy message lỗi từ response (backend ResponseError hoặc AxiosError) */
export function getErrorMessage(error: unknown): string {
  if (axios.isAxiosError(error)) {
    const data = error.response?.data as ResponseError | undefined
    if (data?.message) return data.message
    if (error.message) return error.message
  }
  if (error && typeof error === "object" && "message" in error) {
    return String((error as { message: unknown }).message)
  }
  return "Đã xảy ra lỗi. Vui lòng thử lại."
}
