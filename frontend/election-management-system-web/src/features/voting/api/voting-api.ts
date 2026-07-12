import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";

export interface VoterElectionItem {
  description?: string;
  electionId: string;
  endAt: string;
  name: string;
  startAt: string;
  status: string;
  voteStatus: string | null;
}

export interface VoterElectionsResponse {
  elections: VoterElectionItem[];
  page: number;
  size: number;
  totalElement: number;
  totalPages: number;
}

interface VoterElectionDetailsResponse {
  electionId: string;
  name: string;
  description?: string;
  status: string;
  startAt: string;
  endAt: string;
  voteStatus: string | null;
  positions: {
    electionPositionId: string;
    positionName: string;
    minSelection: number;
    maxSelection: number;
    description?: string;
    candidates: {
      id: string;
      fullName: string;
      email: string;
    }[];
  }[];
}

export const voterApi = {
  async getMyElections(userId: string, status?: string) {
    const { data } = await apiClient.get<ApiResponse<VoterElectionsResponse>>(
      "/voters/elections",
      {
        params: { status },
        headers: { "x-userId": userId },
      }
    );
    return data.data;
  },

  async getVoterElection(userId: string, electionId: string) {
    const { data } = await apiClient.get<
      ApiResponse<VoterElectionDetailsResponse>
    >(`/voters/elections/${electionId}`, {
      headers: { "x-userId": userId },
    });
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
