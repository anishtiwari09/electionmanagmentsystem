import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";

import type { CreateElectionRequest } from "../types/create-election-request";
import type { CreateElectionResponse } from "../types/create-election-response";
import type { ElectionResponse } from "../types/election";
import type { ElectionDetails } from "../types/election-details";
import type { GetElectionsRequest } from "../types/get-elections-request";
import { CreateElectionPositionRequest } from "../types/create-election-position-request";

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

  async getById(userId: string, publicId: string) {
    const { data } = await apiClient.get<ApiResponse<ElectionDetails>>(
      `/elections/${publicId}`,
      {
        headers: {
          "x-userId": userId,
        },
      }
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

  async createPositions(
    userId: string,
    electionPublicId: string,
    request: CreateElectionPositionRequest
  ) {
    const { data } = await apiClient.post<ApiResponse<void>>(
      `/elections/${electionPublicId}/positions`,
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
