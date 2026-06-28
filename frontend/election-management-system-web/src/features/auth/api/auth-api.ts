import { apiClient } from "@/api/client";

import type { RegisterRequest } from "../types/register-request";
import type { RegisterResponse } from "../types/register-response";
import { UserDetails } from "../types/user-details";

export const authApi = {
  async registerOrLogin(request: RegisterRequest) {
    const { data } = await apiClient.post<UserDetails>("/auth/login", request);

    return data;
  },
  async register(request: RegisterRequest) {
    const { data } = await apiClient.post<UserDetails>(
      "/auth/register",
      request
    );

    return data;
  },
};
