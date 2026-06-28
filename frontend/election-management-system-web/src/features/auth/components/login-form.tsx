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
import { ApiErrorResponse } from "@/api/types";
import { LoginFormData, loginSchema } from "../schemas/login-schema";
import { UserDetails } from "../types/user-details";
import { useEffect } from "react";

export function LoginForm() {
  const router = useRouter();

  const [userData, setUserData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<LoginFormData>({
    resolver: zodResolver(loginSchema),
    defaultValues: {
      email: "",
    },
  });

  const registerMutation = useMutation({
    mutationFn: authApi.registerOrLogin,

    onSuccess: (data) => {
      setUserData(data);

      toast.success("Welcome!");

      router.push("/dashboard");
    },

    onError: (error: ApiErrorResponse) => {
      console.log({ error: error.code });
      if (error.code === "USER_NOT_FOUND") {
        router.replace("/auth/register");
      }
      toast.error(error.message);
    },
  });

  const onSubmit = (data: LoginFormData) => {
    registerMutation.mutate(data);
  };

  // useEffect(() => {
  //   if (userData?.publicId) {
  //     router.push("/dashboard");
  //   }
  // }, [userData, router]);
  return (
    <div className="bg-card w-full max-w-md rounded-xl border p-8 shadow-sm">
      <div className="mb-8 space-y-2 text-center">
        <h1 className="text-3xl font-bold">Election Management</h1>

        <p className="text-muted-foreground">Enter your email to continue</p>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-5">
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
