"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

import type { ApiErrorResponse } from "@/api/types";
import { voterApi } from "@/features/election/api/voter-api";
import { queryKeys } from "@/api/query-keys";

interface UseBulkUploadVotersParams {
  userId: string;
  electionId: string;
  onSuccess?: () => void;
}

export function useBulkUploadVoters({
  userId,
  electionId,
  onSuccess,
}: UseBulkUploadVotersParams) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (file: File) => {
      return voterApi.uploadBulkVoters(userId, electionId, file);
    },

    onSuccess: () => {
      queryClient.refetchQueries({
        queryKey: queryKeys.elections.details(electionId),
      });
      toast.success("Voters uploaded successfully");
      onSuccess?.();
    },

    onError: (error: ApiErrorResponse) => {
      toast.error(error.message || "Failed to upload voters");
    },
  });
}
