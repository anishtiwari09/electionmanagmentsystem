import type { ElectionPositionWithCandidate } from "@/features/elections/types";
import type { ElectionVoter } from "@/features/election/types/election-voter";

export interface VoterCandidate {
  candidateId: string;
  email: string;
  firstName: string;
  lastName: string;
  profileImage?: string;
  positionId: string;
}

export interface VoterPosition {
  electionPositionId: string;
  positionName: string;
  minSelection: number;
  maxSelection: number;
  description?: string;
  candidates: VoterCandidate[];
}

export interface VoteSelection {
  positionId: string;
  candidateIds: string[];
}

export interface VoterElection {
  electionId: string;
  name: string;
  description?: string;
  status: string;
  startAt: string;
  endAt: string;
  hasVoted: boolean;
}

export function toVoterPositions(
  positions: ElectionPositionWithCandidate[]
): VoterPosition[] {
  return positions.map((pos) => ({
    electionPositionId: pos.electionPositionId,
    positionName: pos.positionName,
    minSelection: pos.minSelection,
    maxSelection: pos.maxSelection,
    description: pos.description,
    candidates: pos.candidates.map((c) => ({
      candidateId: c.id,
      email: c.email,
      firstName: c.fullName.split(" ")[0] || c.fullName,
      lastName: c.fullName.split(" ").slice(1).join(" ") || "",
      positionId: pos.electionPositionId,
    })),
  }));
}

export function toVoterElection(
  election: {
    electionId: string;
    name: string;
    description?: string;
    status: string;
    startAt: string;
    endAt: string;
  },
  voter?: ElectionVoter
): VoterElection {
  return {
    electionId: election.electionId,
    name: election.name,
    description: election.description,
    status: election.status,
    startAt: election.startAt,
    endAt: election.endAt,
    hasVoted: voter?.hasVoted ?? false,
  };
}
