import type { GetElectionsRequest } from "@/features/election/types/get-elections-request";

export const queryKeys = {
  auth: {
    all: ["auth"] as const,

    me: () => [...queryKeys.auth.all, "me"] as const,
  },

  elections: {
    all: ["elections"] as const,

    lists: () => [...queryKeys.elections.all, "list"] as const,

    list: (filters?: GetElectionsRequest) =>
      [...queryKeys.elections.lists(), filters ?? {}] as const,

    details: () => [...queryKeys.elections.all, "detail"] as const,

    detail: (id: string) => [...queryKeys.elections.details(), id] as const,
  },
} as const;
