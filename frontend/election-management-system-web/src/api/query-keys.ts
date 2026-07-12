import type { GetElectionsRequest } from "@/features/election/types/get-elections-request";

export const queryKeys = {
  auth: {
    all: ["auth"] as const,

    me: () => [...queryKeys.auth.all, "me"] as const,
  },

  elections: {
    list: (filters?: unknown) => ["elections", "list", filters] as const,

    details: (publicId: string) => ["elections", "details", publicId] as const,

    create: () => ["elections", "create"] as const,
  },

  voter: {
    elections: (status?: string) => ["voter", "elections", status] as const,
  },
} as const;
