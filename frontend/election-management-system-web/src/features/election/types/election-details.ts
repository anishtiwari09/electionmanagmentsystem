import {
  ElectionCandidate,
  ElectionPositionWithCandidate,
} from "@/features/elections/types";
import { ElectionStatus } from "./election-status";

import { ElectionVoter } from "./election-voter";

export interface ElectionDetails {
  electionId: string;

  name: string;

  description: string;

  status: ElectionStatus;

  startAt: string;

  endAt: string;

  createdAt: string;

  updatedAt: string;
  positions: ElectionPositionWithCandidate[];
  candidates: ElectionCandidate[];

  voters: ElectionVoter[];
}
