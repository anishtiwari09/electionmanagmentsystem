import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";
import type { ElectionResponse } from "@/features/election/types/election";

export const voterApi = {
  async getMyElections(userId: string, status?: string) {
    const { data } = await apiClient.get<ApiResponse<ElectionResponse>>(
      "/voter/elections",
      {
        params: { status },
        headers: { "x-userId": userId },
      }
    );
    return data.data;
  },

  async castVote(
    userId: string,
    electionId: string,
    selections: { positionId: string; candidateId: string }[]
  ) {
    const { data } = await apiClient.post<ApiResponse<void>>(
      `/elections/${electionId}/vote`,
      { selections },
      { headers: { "x-userId": userId } }
    );
    return data.data;
  },
};
