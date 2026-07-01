"use client";

import { useQuery } from "@tanstack/react-query";

import { queryKeys } from "@/api/query-keys";

import { electionApi } from "../api/election-api";

export function useElection(userId: string, electionPublicId: string) {
  return useQuery({
    queryKey: queryKeys.elections.details(electionPublicId),

    queryFn: () => electionApi.getById(userId, electionPublicId),

    enabled: !!userId && !!electionPublicId,
  });
}
