import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";

import type { Election } from "../types/election";
import type { GetElectionsRequest } from "../types/get-elections-request";

export const electionApi = {
  async getAll(userId: string, filters?: GetElectionsRequest) {
    const { data } = await apiClient.get<ApiResponse<Election[]>>(
      "/elections",
      {
        params: filters,
        headers: {
          "x-userId": userId,
        },
      }
    );

    return data.data;
  },

  async getById(id: string) {
    const { data } = await apiClient.get<ApiResponse<Election>>(
      `/elections/${id}`
    );

    return data.data;
  },
};
