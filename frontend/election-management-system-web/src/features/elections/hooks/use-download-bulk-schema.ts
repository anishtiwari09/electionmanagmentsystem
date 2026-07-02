"use client";

import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import { candidateApi } from "@/features/election/api/candidate-api";
import type { DownloadBulkSchemaRequest } from "../types";

interface UseDownloadBulkSchemaParams {
  userId: string;
  electionId: string;
}

export function useDownloadBulkSchema({
  userId,
  electionId,
}: UseDownloadBulkSchemaParams) {
  return useMutation({
    mutationFn: async (data: DownloadBulkSchemaRequest) => {
      const blob = await candidateApi.downloadBulkSchema(userId, data);

      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "bulk-candidates-schema.csv";
      document.body.appendChild(a);
      a.click();
      document.body.removeChild(a);
      window.URL.revokeObjectURL(url);
    },

    onError: () => {
      toast.error("Failed to download schema");
    },
  });
}
