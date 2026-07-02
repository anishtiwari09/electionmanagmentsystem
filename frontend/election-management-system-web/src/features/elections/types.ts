export interface ElectionPosition {
  electionPositionId: string;
  positionName: string;
  minSelection: number;
  maxSelection: number;
  description?: string;
}
export interface ElectionCandidateDto {
  id: string;
  fullName: string;
  email: string;
}
export interface ElectionCandidate extends ElectionCandidateDto {
  positionId: string;
}
export interface ElectionVoter {
  id: string;
  fullName: string;
  email: string;
}

export interface Election {
  id: string;
  title: string;
  description: string;
  startDate: string;
  endDate: string;
  positions: ElectionPosition[];
  candidates: ElectionCandidate[];
  voters: ElectionVoter[];
}

export interface ElectionPositionWithCandidate extends ElectionPosition {
  candidates: ElectionCandidateDto[];
}

export interface BulkUploadPositionItem {
  electionPositionId: string;
  numberOfCandidates: number;
}

export interface DownloadBulkSchemaRequest {
  positions: BulkUploadPositionItem[];
  electionId: string;
}
