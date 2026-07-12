import type { ElectionPositionWithCandidate } from "@/features/elections/types";

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

export interface VoterElectionDetails {
  electionId: string;
  name: string;
  description?: string;
  status: string;
  startAt: string;
  endAt: string;
  voteStatus: string | null;
  positions: VoterPosition[];
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
