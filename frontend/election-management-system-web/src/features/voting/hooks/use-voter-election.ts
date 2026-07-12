"use client";

import { useQuery } from "@tanstack/react-query";
import { voterApi } from "../api/voting-api";
import { queryKeys } from "@/api/query-keys";
import { toVoterPositions } from "../types";

export function useVoterElection(electionId: string, userId: string) {
  return useQuery({
    queryKey: [...queryKeys.elections.details(electionId), "voter"],
    queryFn: async () => {
      const data = await voterApi.getVoterElection(userId, electionId);
      return {
        election: {
          electionId: data.electionId,
          name: data.name,
          description: data.description,
          status: data.status,
          startAt: data.startAt,
          endAt: data.endAt,
          hasVoted: data.voteStatus !== null,
        },
        positions: toVoterPositions(data.positions),
      };
    },
    enabled: !!electionId && !!userId,
  });
}
