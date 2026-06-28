"use client";

import { Badge } from "@/components/ui/badge";
import {
  Card,
  CardContent,
  CardDescription,
  CardHeader,
  CardTitle,
} from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";

import { Election } from "../types/election";
import { useRouter } from "next/router";
import { useQuery } from "@tanstack/react-query";
import { queryKeys } from "@/api/query-keys";
import { electionApi } from "../api/election-api";
import { GetElectionsRequest } from "../types/get-elections-request";
import { ElectionStatus } from "../types/election-status";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { UserDetails } from "@/features/auth/types/user-details";
import { CreateElectionDialog } from "./create-election-dialog";

interface ElectionListProps {
  elections: Election[];
  isLoading?: boolean;
  onElectionClick?: (election: Election) => void;
}

export function ElectionList({
  elections,
  isLoading = false,
  onElectionClick,
}: Readonly<ElectionListProps>) {
  if (isLoading) {
    return (
      <div className="grid gap-4">
        {Array.from({ length: 4 }).map((_, index) => (
          <ElectionCardSkeleton key={index} />
        ))}
      </div>
    );
  }

  if (!elections.length) {
    return (
      <Card>
        <CardContent className="flex h-56 flex-col items-center justify-center text-center">
          <h3 className="text-lg font-semibold">No elections found</h3>

          <p className="text-muted-foreground mt-2 text-sm">
            Create your first election to get started.
          </p>
        </CardContent>
      </Card>
    );
  }

  return (
    <div className="grid gap-4">
      {elections.map((election) => (
        <ElectionCard
          key={election.id}
          election={election}
          onClick={onElectionClick}
        />
      ))}
    </div>
  );
}

interface ElectionCardProps {
  election: Election;
  onClick?: (election: Election) => void;
}

function ElectionCard({ election, onClick }: Readonly<ElectionCardProps>) {
  return (
    <Card
      onClick={() => onClick?.(election)}
      className="hover:border-primary cursor-pointer transition-all hover:shadow-md"
    >
      <CardHeader className="flex flex-row items-start justify-between space-y-0">
        <div className="space-y-1">
          <CardTitle>{election.title}</CardTitle>

          {election.description && (
            <CardDescription>{election.description}</CardDescription>
          )}
        </div>

        <StatusBadge status={election.status} />
      </CardHeader>

      <CardContent>
        <p className="text-muted-foreground text-sm">
          Created on {new Date(election.createdAt).toLocaleDateString()}
        </p>
      </CardContent>
    </Card>
  );
}

function ElectionCardSkeleton() {
  return (
    <Card>
      <CardHeader className="space-y-3">
        <Skeleton className="h-5 w-48" />
        <Skeleton className="h-4 w-full" />
      </CardHeader>

      <CardContent>
        <Skeleton className="h-4 w-36" />
      </CardContent>
    </Card>
  );
}

interface StatusBadgeProps {
  status: Election["status"];
}

function StatusBadge({ status }: Readonly<StatusBadgeProps>) {
  switch (status) {
    case "ACTIVE":
      return <Badge className="bg-green-600 hover:bg-green-600">Active</Badge>;

    case "COMPLETED":
      return <Badge variant="secondary">Completed</Badge>;

    default:
      return <Badge variant="outline">Draft</Badge>;
  }
}

export default function ElectionListMain() {
  const [userData, _] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );
  const filters: GetElectionsRequest = {
    status: [ElectionStatus.ACTIVE, ElectionStatus.DRAFT],
  };
  const {
    data: elections = [],
    isLoading,
    error,
  } = useQuery({
    queryKey: queryKeys.elections.list(filters),

    queryFn: () => electionApi.getAll(userData.userId, filters),
  });
  return (
    <div className="space-y-8">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-3xl font-bold tracking-tight">Elections</h1>

          <p className="text-muted-foreground mt-1">
            Manage and organize your elections.
          </p>
        </div>

        <CreateElectionDialog />
      </div>

      <ElectionList elections={elections} isLoading={isLoading} />
    </div>
  );
}
