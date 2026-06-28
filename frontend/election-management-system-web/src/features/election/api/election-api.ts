import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";

import type { CreateElectionRequest } from "../types/create-election-request";
import type { CreateElectionResponse } from "../types/create-election-response";
import type { Election, ElectionResponse } from "../types/election";
import type { GetElectionsRequest } from "../types/get-elections-request";

export const electionApi = {
  async getAll(userId: string, filters?: GetElectionsRequest) {
    const { data } = await apiClient.get<ApiResponse<ElectionResponse>>(
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

  async create(userId: string, request: CreateElectionRequest) {
    const { data } = await apiClient.post<ApiResponse<CreateElectionResponse>>(
      "/elections",
      request,
      {
        headers: {
          "x-userId": userId,
        },
      }
    );

    return data.data;
  },
};
