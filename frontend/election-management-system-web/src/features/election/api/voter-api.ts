import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";

export const voterApi = {
  async downloadBulkSchema(userId: string, electionId: string) {
    const response = await apiClient.post(
      `/voters/generate-template`,
      { electionId },
      {
        headers: {
          "x-userId": userId,
        },
        responseType: "blob",
      }
    );

    return response.data as Blob;
  },

  async uploadBulkVoters(userId: string, electionPublicId: string, file: File) {
    const formData = new FormData();
    formData.append("file", file);

    const { data } = await apiClient.post<ApiResponse<void>>(
      `/elections/${electionPublicId}/voters/upload`,
      formData,
      {
        headers: {
          "x-userId": userId,
          "Content-Type": "multipart/form-data",
        },
      }
    );

    return data.data;
  },
};
