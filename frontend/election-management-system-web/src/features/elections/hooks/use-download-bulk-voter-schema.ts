"use client";

import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import { voterApi } from "@/features/election/api/voter-api";

interface UseDownloadBulkVoterSchemaParams {
  userId: string;
  electionId: string;
}

export function useDownloadBulkVoterSchema({
  userId,
  electionId,
}: UseDownloadBulkVoterSchemaParams) {
  return useMutation({
    mutationFn: async () => {
      const blob = await voterApi.downloadBulkSchema(userId, electionId);

      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = "bulk-voters-schema.csv";
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
