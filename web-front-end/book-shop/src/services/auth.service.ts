import axiosClient from "@/configurations/axios.config"
import type { RegisterRequest, AuthRegisterResponse } from "@/types/auth.type"

export const authService = {
  /** Đăng ký tài khoản – POST /api/v1/auth/register */
  register: async (request: RegisterRequest) => {
    const response = await axiosClient.post<AuthRegisterResponse>(
      "/api/v1/auth/register",
      request
    )
    return response
  },
}
