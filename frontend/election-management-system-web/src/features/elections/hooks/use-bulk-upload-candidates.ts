"use client";

import { useMutation, useQueryClient } from "@tanstack/react-query";
import { toast } from "sonner";

import type { ApiErrorResponse } from "@/api/types";
import { candidateApi } from "@/features/election/api/candidate-api";
import { queryKeys } from "@/api/query-keys";

interface UseBulkUploadCandidatesParams {
  userId: string;
  electionId: string;
  onSuccess?: () => void;
}

export function useBulkUploadCandidates({
  userId,
  electionId,
  onSuccess,
}: UseBulkUploadCandidatesParams) {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (file: File) => {
      return candidateApi.uploadBulkCandidates(userId, electionId, file);
    },

    onSuccess: () => {
      queryClient.refetchQueries({
        queryKey: queryKeys.elections.details(electionId),
      });
      toast.success("Candidates uploaded successfully");
      onSuccess?.();
    },

    onError: (error: ApiErrorResponse) => {
      toast.error(error.message || "Failed to upload candidates");
    },
  });
}
