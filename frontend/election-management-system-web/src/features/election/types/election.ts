export interface Election {
  electionId: string;
  name: string;
  description?: string;
  status: "DRAFT" | "ACTIVE" | "COMPLETED";
  createdAt: string;
}

export interface ElectionResponse {
  elections: Election[];
  totalItems: number;
  page: number;
  size: number;
  totalElement: 2;
  totalPages: 1;
}
