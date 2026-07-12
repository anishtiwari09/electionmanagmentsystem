"use client";

import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { Election } from "@/features/election/types/election";
import { CheckCircle2, Vote, Clock } from "lucide-react";

interface VoterElectionListProps {
  elections: Election[];
  isLoading?: boolean;
  onElectionClick?: (election: Election) => void;
}

export function VoterElectionList({
  elections,
  isLoading = false,
  onElectionClick,
}: VoterElectionListProps) {
  return (
    <div className="grid gap-4">
      {isLoading ? (
        Array.from({ length: 3 }).map((_, i) => (
          <Card key={i}>
            <CardHeader>
              <Skeleton className="h-5 w-48" />
            </CardHeader>
            <CardContent>
              <Skeleton className="h-4 w-32" />
            </CardContent>
          </Card>
        ))
      ) : !elections.length ? (
        <Card>
          <CardContent className="flex h-56 flex-col items-center justify-center text-center">
            <Vote className="text-muted-foreground mb-3 h-10 w-10" />
            <h3 className="text-lg font-semibold">No elections available</h3>
            <p className="text-muted-foreground mt-1 text-sm">
              There are no elections for you to vote in right now.
            </p>
          </CardContent>
        </Card>
      ) : (
        elections.map((election) => (
          <VoterElectionCard
            key={election.electionId}
            election={election}
            onClick={onElectionClick}
          />
        ))
      )}
    </div>
  );
}

interface VoterElectionCardProps {
  election: Election;
  onClick?: (election: Election) => void;
}

function VoterElectionCard({ election, onClick }: VoterElectionCardProps) {
  return (
    <Card
      onClick={() => onClick?.(election)}
      className="hover:border-primary cursor-pointer transition-all hover:shadow-md"
    >
      <CardHeader>
        <div className="flex items-start justify-between">
          <CardTitle>{election.name}</CardTitle>
          <StatusBadge status={election.status} />
        </div>
        {election.description && (
          <p className="text-muted-foreground mt-1 text-sm">
            {election.description}
          </p>
        )}
      </CardHeader>
      <CardContent>
        <div className="text-muted-foreground flex items-center text-sm">
          <Clock className="mr-1.5 h-4 w-4" />
          Created on {new Date(election.createdAt).toLocaleDateString("en-US")}
        </div>
      </CardContent>
    </Card>
  );
}

interface StatusBadgeProps {
  status: Election["status"];
}

function StatusBadge({ status }: StatusBadgeProps) {
  switch (status) {
    case "ACTIVE":
      return (
        <Badge className="bg-green-600 hover:bg-green-600">
          <CheckCircle2 className="mr-1 h-3 w-3" />
          Active
        </Badge>
      );
    case "COMPLETED":
      return <Badge variant="secondary">Completed</Badge>;
    default:
      return <Badge variant="outline">Draft</Badge>;
  }
}
