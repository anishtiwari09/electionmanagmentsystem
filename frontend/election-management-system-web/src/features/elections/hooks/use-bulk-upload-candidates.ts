"use client";

import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import type { ApiErrorResponse } from "@/api/types";
import { candidateApi } from "@/features/election/api/candidate-api";

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
  return useMutation({
    mutationFn: (file: File) =>
      candidateApi.uploadBulkCandidates(userId, electionId, file),

    onSuccess: () => {
      toast.success("Candidates uploaded successfully");
      onSuccess?.();
    },

    onError: (error: ApiErrorResponse) => {
      toast.error(error.message || "Failed to upload candidates");
    },
  });
}
