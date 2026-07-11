"use client";

import { useElection } from "../hooks/use-election";
import { PageHeader } from "./page-header";
import { PositionsSection } from "./positions-section";
import { CandidatesSection } from "./candidates-section";
import { VotersSection } from "./voters-section";
import { useMemo } from "react";
import {
  ElectionCandidate,
  ElectionPositionWithCandidate,
  ElectionVoter,
} from "../types";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { UserDetails } from "@/features/auth/types/user-details";
import { STORAGE_KEYS } from "@/constants/storage-keys";

type Props = {
  electionId: string;
};

export function ElectionDetails({ electionId }: Props) {
  const [userData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const { data, isPending } = useElection(electionId, userData?.userId);

  const { positions, candidates, voters } = useMemo(() => {
    const positions: ElectionPositionWithCandidate[] = [];
    const candidates: ElectionCandidate[] = [];
    const voters: ElectionVoter[] = [];
    if (!isPending && data) {
      data.positions.forEach((position) => {
        positions.push(position);
        position?.candidates.forEach((candidate) => {
          candidates.push({
            ...candidate,
            positionId: position.electionPositionId,
          });
        });
      });
      data.voters?.forEach((voter) => {
        voters.push({
          id: voter.publicId,
          fullName: `${voter.firstName} ${voter.lastName}`,
          email: voter.email,
        });
      });
    }
    return {
      positions,
      candidates,
      voters,
    };
  }, [data, isPending]);

  if (isPending) {
    return <div className="container mx-auto max-w-7xl py-8">Loading...</div>;
  }

  if (!data) return null;

  return (
    <div className="container mx-auto max-w-7xl space-y-8 py-8">
      <PageHeader
        title={data.name}
        description={data.description}
        startDate={data.startAt}
        endDate={data.startAt}
      />

      <PositionsSection
        positions={data.positions}
        electionId={electionId}
        userId={userData.userId}
      />

      <CandidatesSection
        candidates={candidates}
        positions={positions}
        electionId={electionId}
      />

      <VotersSection voters={voters} electionId={electionId} />
    </div>
  );
}
