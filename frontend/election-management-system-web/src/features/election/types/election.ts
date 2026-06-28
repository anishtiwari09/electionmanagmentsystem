export interface Election {
  id: string;
  title: string;
  description?: string;
  status: "DRAFT" | "ACTIVE" | "COMPLETED";
  createdAt: string;
}
