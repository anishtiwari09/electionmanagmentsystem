"use client";

import { useQuery } from "@tanstack/react-query";
import { electionApi } from "@/features/election/api/election-api";
import { queryKeys } from "@/api/query-keys";

export function useElection(id: string, userId: string) {
  return useQuery({
    queryKey: queryKeys.elections.details(id),
    queryFn: () => {
      return electionApi.getById(userId, id);
    },
    enabled: !!id && !!userId,
  });
}
