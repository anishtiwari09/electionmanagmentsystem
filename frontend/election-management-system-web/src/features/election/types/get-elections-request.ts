import { ElectionStatus } from "./election-status";

export interface GetElectionsRequest {
  status?: ElectionStatus[];

  search?: string;

  page?: number;

  size?: number;
}
