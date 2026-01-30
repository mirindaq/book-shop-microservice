import { zodResolver } from "@hookform/resolvers/zod"
import { useForm } from "react-hook-form"
import { z } from "zod"
import { toast } from "sonner"
import { Button } from "@/components/ui/button"
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/components/ui/form"
import { Input } from "@/components/ui/input"
import { useMutation } from "@/hooks/useMutation"
import { authService } from "@/services/auth.service"
import type { RegisterRequest } from "@/types/auth.type"
import type { AuthRegisterResponse } from "@/types/auth.type"
import { getErrorMessage } from "@/utils/error.util"

const registerSchema = z.object({
  username: z
    .string()
    .min(1, "Tên đăng nhập là bắt buộc")
    .min(3, "Tên đăng nhập tối thiểu 3 ký tự")
    .max(50, "Tên đăng nhập tối đa 50 ký tự"),
  password: z
    .string()
    .min(1, "Mật khẩu là bắt buộc")
    .min(6, "Mật khẩu tối thiểu 6 ký tự"),
  email: z.string().min(1, "Email là bắt buộc").email("Email không hợp lệ"),
  firstName: z.string().max(100).optional().or(z.literal("")),
  lastName: z.string().max(100).optional().or(z.literal("")),
})

type RegisterFormValues = z.infer<typeof registerSchema>

function RegisterPage() {
  const form = useForm<RegisterFormValues>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      username: "",
      password: "",
      email: "",
      firstName: "",
      lastName: "",
    },
  })

  const registerMutation = useMutation<AuthRegisterResponse>(
    (variables) => authService.register(variables as RegisterRequest),
    {
      onSuccess: (data) => {
        toast.success(data?.message ?? "Đăng ký thành công.")
      },
      onError: (error) => {
        toast.error(getErrorMessage(error))
      },
    }
  )

  function onSubmit(values: RegisterFormValues) {
    const body: RegisterRequest = {
      username: values.username.trim(),
      password: values.password,
      email: values.email.trim(),
      firstName: values.firstName?.trim() || undefined,
      lastName: values.lastName?.trim() || undefined,
    }
    registerMutation.mutate(body)
  }

  return (
    <div className="mx-auto w-full max-w-sm space-y-6 py-12">
      <div className="space-y-2 text-center">
        <h1 className="text-2xl font-semibold">Đăng ký tài khoản</h1>
        <p className="text-muted-foreground text-sm">
          Điền thông tin để tạo tài khoản mới
        </p>
      </div>
      <Form {...form}>
        <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
          <FormField
            control={form.control}
            name="username"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tên đăng nhập</FormLabel>
                <FormControl>
                  <Input placeholder="username" autoComplete="username" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="password"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Mật khẩu</FormLabel>
                <FormControl>
                  <Input
                    type="password"
                    placeholder="••••••••"
                    autoComplete="new-password"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="email"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Email</FormLabel>
                <FormControl>
                  <Input
                    type="email"
                    placeholder="email@example.com"
                    autoComplete="email"
                    {...field}
                  />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="firstName"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Họ (tùy chọn)</FormLabel>
                <FormControl>
                  <Input placeholder="Họ" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <FormField
            control={form.control}
            name="lastName"
            render={({ field }) => (
              <FormItem>
                <FormLabel>Tên (tùy chọn)</FormLabel>
                <FormControl>
                  <Input placeholder="Tên" {...field} />
                </FormControl>
                <FormMessage />
              </FormItem>
            )}
          />
          <Button
            type="submit"
            className="w-full"
            disabled={registerMutation.isLoading}
          >
            {registerMutation.isLoading ? "Đang xử lý..." : "Đăng ký"}
          </Button>
        </form>
      </Form>
    </div>
  )
}

export default RegisterPage
