"use client";

import { useSyncExternalStore } from "react";
import { useQuery } from "@tanstack/react-query";
import { useRouter } from "next/navigation";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { queryKeys } from "@/api/query-keys";
import { UserDetails } from "@/features/auth/types/user-details";
import { voterApi } from "@/features/voting/api/voting-api";
import { VoterElectionList } from "@/features/voting/components/voter-election-list";
import { Vote } from "lucide-react";

function useHydrated() {
  return useSyncExternalStore(
    () => () => {},
    () => true,
    () => false
  );
}

export default function VoterElectionsPage() {
  const router = useRouter();
  const hydrated = useHydrated();
  const [userData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const { data, isLoading } = useQuery({
    queryKey: queryKeys.voter.elections("ACTIVE"),
    queryFn: () => voterApi.getMyElections(userData.userId, "ACTIVE"),
    enabled: !!userData.userId,
  });

  return (
    <div className="container mx-auto max-w-4xl py-8">
      <div className="mb-8">
        <div className="flex items-center gap-3">
          <div className="bg-primary/10 flex size-12 items-center justify-center rounded-xl">
            <Vote className="text-primary h-6 w-6" />
          </div>
          <div>
            <h1 className="text-3xl font-bold tracking-tight">
              Cast Your Vote
            </h1>
            <p className="text-muted-foreground mt-1">
              Select an election below to view candidates and cast your vote.
            </p>
          </div>
        </div>
      </div>

      {hydrated && (
        <VoterElectionList
          elections={data?.elections || []}
          isLoading={isLoading}
          onElectionClick={(election) => {
            if (election.status === "ACTIVE") {
              router.push(`/dashboard/vote/${election.electionId}`);
            }
          }}
        />
      )}
    </div>
  );
}
