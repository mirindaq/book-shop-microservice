import axios from "axios"
import { toast } from "sonner"

const IDENTITY_BASE_URL =
  import.meta.env.VITE_IDENTITY_API_URL ?? "http://localhost:8081/identity"

const axiosClient = axios.create({
  baseURL: IDENTITY_BASE_URL,
  headers: { "Content-Type": "application/json" },
  timeout: 10000,
})

axiosClient.interceptors.request.use(
  (config) => {
    return config
  },
  (error) => Promise.reject(error)
)

axiosClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response) {
      if (error.response.status === 403) {
        toast.error("Không có quyền truy cập")
      } else if (error.response.status === 500) {
        toast.error("Lỗi server, vui lòng thử lại sau")
      }
      return Promise.reject(error.response.data ?? error)
    }
    return Promise.reject(error)
  }
)

export default axiosClient
