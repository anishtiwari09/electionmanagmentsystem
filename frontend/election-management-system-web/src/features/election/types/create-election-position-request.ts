export interface CreateElectionPositionRequest {
  positions: {
    positionName: string;
    description?: string;
    minSelection: number;
    maxSelection: number;
  }[];
}
