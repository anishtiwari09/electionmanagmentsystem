import { apiClient } from "@/api/client";
import type { ApiResponse } from "@/api/types";
import { DownloadBulkSchemaRequest } from "@/features/elections/types";

export const candidateApi = {
  async downloadBulkSchema(
    userId: string,

    request: DownloadBulkSchemaRequest
  ) {
    const response = await apiClient.post(
      `/candidates/generate-template`,
      request,
      {
        headers: {
          "x-userId": userId,
        },
        responseType: "blob",
      }
    );

    return response.data as Blob;
  },

  async uploadBulkCandidates(
    userId: string,
    electionPublicId: string,
    file: File
  ) {
    const formData = new FormData();
    formData.append("file", file);

    const { data } = await apiClient.post<ApiResponse<void>>(
      `/elections/${electionPublicId}/candidates/upload`,
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
