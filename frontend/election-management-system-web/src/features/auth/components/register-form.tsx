"use client";

import { zodResolver } from "@hookform/resolvers/zod";
import { useMutation } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useForm } from "react-hook-form";
import { toast } from "sonner";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { useLocalStorage } from "@/hooks/use-local-storage";

import { authApi } from "../api/auth-api";
import {
  registerSchema,
  type RegisterFormData,
} from "../schemas/register-schema";
import { ApiErrorResponse } from "@/api/types";
import { UserDetails } from "../types/user-details";

export function RegisterForm() {
  const router = useRouter();

  const [, setUserData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {
      firstName: "",
      lastName: "",
      email: "",
      userId: "",
    }
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<RegisterFormData>({
    resolver: zodResolver(registerSchema),
    defaultValues: {
      email: "",
      firstName: "",
      lastName: "",
    },
  });

  const registerMutation = useMutation({
    mutationFn: authApi.register,

    onSuccess: (data: UserDetails) => {
      //   console.log(data:UserDetails);
      setUserData(data);

      toast.success("Welcome!");

      router.push("/dashboard");
    },

    onError: (error: ApiErrorResponse) => {
      toast.error(error.message);
    },
  });

  const onSubmit = (data: RegisterFormData) => {
    registerMutation.mutate(data);
  };

  return (
    <div className="bg-card w-full max-w-md rounded-xl border p-8 shadow-sm">
      <div className="mb-8 space-y-2 text-center">
        <h1 className="text-3xl font-bold">Election Management</h1>

        <p className="text-muted-foreground">Enter your email to continue</p>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
        <div className="space-y-2">
          <Input
            type="text"
            autoComplete="firstName"
            placeholder="Enter your first name"
            {...register("firstName")}
          />

          {errors.firstName && (
            <p className="text-destructive text-sm">
              {errors.firstName.message}
            </p>
          )}
        </div>
        <div className="space-y-2">
          <Input
            type="text"
            autoComplete="lastName"
            placeholder="Enter your last name"
            {...register("lastName")}
          />

          {errors.lastName && (
            <p className="text-destructive text-sm">
              {errors.lastName.message}
            </p>
          )}
        </div>
        <div className="space-y-2">
          <Input
            type="email"
            autoComplete="email"
            placeholder="Enter your email"
            {...register("email")}
          />

          {errors.email && (
            <p className="text-destructive text-sm">{errors.email.message}</p>
          )}
        </div>

        <Button
          type="submit"
          className="w-full"
          disabled={registerMutation.isPending}
        >
          {registerMutation.isPending ? "Please wait..." : "Continue"}
        </Button>
      </form>
    </div>
  );
}
