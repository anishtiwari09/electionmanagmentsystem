"use client";

import { useMutation } from "@tanstack/react-query";
import { toast } from "sonner";

import type { ApiErrorResponse } from "@/api/types";
import { electionApi } from "@/features/election/api/election-api";
import { CreateElectionPositionFormData } from "@/features/election/schemas/create-election-position-schema";

interface UseCreatePositionsParams {
  userId: string;
  electionId: string;
  onSuccess?: () => void;
}

export function useCreatePositions({
  userId,
  electionId,
  onSuccess,
}: UseCreatePositionsParams) {
  return useMutation({
    mutationFn: (data: CreateElectionPositionFormData) =>
      electionApi.createPositions(userId, electionId, data),

    onSuccess: () => {
      toast.success("Positions created successfully");
      onSuccess?.();
    },

    onError: (error: ApiErrorResponse) => {
      toast.error(error.message || "Failed to create positions");
    },
  });
}
