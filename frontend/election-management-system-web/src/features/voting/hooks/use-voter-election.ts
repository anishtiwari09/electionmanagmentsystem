"use client";

import { useQuery } from "@tanstack/react-query";
import { electionApi } from "@/features/election/api/election-api";
import { queryKeys } from "@/api/query-keys";
import { toVoterPositions, toVoterElection } from "../types";

export function useVoterElection(electionId: string, userId: string) {
  return useQuery({
    queryKey: [...queryKeys.elections.details(electionId), "voter"],
    queryFn: async () => {
      const data = await electionApi.getById(userId, electionId);
      const matchingVoter = data.voters?.find(
        (v) => v.publicId === userId || v.email === userId
      );
      return {
        election: toVoterElection(data, matchingVoter),
        positions: toVoterPositions(data.positions),
      };
    },
    enabled: !!electionId && !!userId,
  });
}
