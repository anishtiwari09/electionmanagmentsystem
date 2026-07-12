"use client";

import { useMemo } from "react";
import { Badge } from "@/components/ui/badge";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { Skeleton } from "@/components/ui/skeleton";
import { CheckCircle2, Vote, Timer } from "lucide-react";
import type { VoterElectionItem } from "../api/voting-api";

interface VoterElectionListProps {
  elections: VoterElectionItem[];
  isLoading?: boolean;
  onElectionClick?: (election: VoterElectionItem) => void;
}

function formatDate(dateStr: string) {
  return new Date(dateStr).toLocaleDateString("en-US", {
    year: "numeric",
    month: "short",
    day: "numeric",
  });
}

function daysLeft(dateStr: string): { text: string; days: number } | null {
  const now = new Date();
  const end = new Date(dateStr);
  const diff = end.getTime() - now.getTime();
  if (diff < 0) return null;
  const days = Math.ceil(diff / (1000 * 60 * 60 * 24));
  if (days === 0) return { text: "Ends today", days: 0 };
  if (days === 1) return { text: "1 day left", days: 1 };
  return { text: `${days} days left`, days };
}

function TimerBadge({ days, text }: { days: number; text: string }) {
  const [color, bg] =
    days <= 1
      ? ["text-red-700", "bg-red-100 dark:bg-red-900/30"]
      : days <= 3
        ? ["text-amber-700", "bg-amber-100 dark:bg-amber-900/30"]
        : ["text-green-700", "bg-green-100 dark:bg-green-900/30"];

  return (
    <span
      className={`inline-flex h-5 shrink-0 items-center gap-1 rounded-4xl border border-transparent px-2 py-0.5 text-xs font-medium whitespace-nowrap ${bg} ${color}`}
    >
      <Timer className="h-3 w-3" />
      {text}
    </span>
  );
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
  election: VoterElectionItem;
  onClick?: (election: VoterElectionItem) => void;
}

function VoterElectionCard({ election, onClick }: VoterElectionCardProps) {
  const remaining = useMemo(() => daysLeft(election.endAt), [election.endAt]);

  return (
    <Card
      onClick={() => onClick?.(election)}
      className="hover:border-primary cursor-pointer transition-all hover:shadow-md"
    >
      <CardHeader>
        <div className="flex items-start justify-between">
          <div className="min-w-0 flex-1">
            <CardTitle>{election.name}</CardTitle>
            {election.description && (
              <p className="text-muted-foreground mt-0.5 text-sm">
                {election.description}
              </p>
            )}
          </div>
          <StatusBadge status={election.status} />
        </div>
      </CardHeader>
      <CardContent>
        <div className="text-muted-foreground flex items-center gap-4 text-sm">
          <span>
            {formatDate(election.startAt)} — {formatDate(election.endAt)}
          </span>
          {election.status === "ACTIVE" && remaining && (
            <TimerBadge days={remaining.days} text={remaining.text} />
          )}
        </div>
      </CardContent>
    </Card>
  );
}

interface StatusBadgeProps {
  status: string;
}

function StatusBadge({ status }: StatusBadgeProps) {
  switch (status) {
    case "ACTIVE":
      return (
        <Badge className="shrink-0 bg-green-600 hover:bg-green-600">
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
