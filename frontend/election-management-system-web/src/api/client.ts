import axios from "axios";

import { env } from "@/config/env";

import { ApiError } from "./api-error";
import type { ApiErrorResponse } from "./types";

export const apiClient = axios.create({
  baseURL: env.NEXT_PUBLIC_API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

apiClient.interceptors.response.use(
  (response) => response,

  (error) => {
    if (error.response) {
      const response = error.response.data as ApiErrorResponse;

      throw new ApiError(
        error.response.status,
        response.code,
        response.message,
        response.data
      );
    }

    throw new ApiError(500, "UNKNOWN_ERROR", "Something went wrong.");
  }
);
