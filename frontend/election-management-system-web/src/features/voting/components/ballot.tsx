"use client";

import { useMemo, useState } from "react";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";

import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { Skeleton } from "@/components/ui/skeleton";
import { CheckCheck, ImageOff, ChevronLeft, Save, Eye } from "lucide-react";
import type { VoterPosition, VoteSelection } from "../types";
import { PreviewDialog } from "./preview-dialog";

type Props = {
  electionName: string;
  positions: VoterPosition[];
  isPending: boolean;
  onSubmit: (selections: VoteSelection[]) => void;
  isSubmitting?: boolean;
  hasVoted: boolean;
};

export function Ballot({
  electionName,
  positions,
  isPending,
  onSubmit,
  isSubmitting,
  hasVoted,
}: Props) {
  const [selections, setSelections] = useState<Map<string, string[]>>(
    () => new Map()
  );
  const [previewOpen, setPreviewOpen] = useState(false);

  const totalSelected = useMemo(() => {
    let count = 0;
    selections.forEach((ids) => {
      count += ids.length;
    });
    return count;
  }, [selections]);

  const totalRequired = useMemo(() => {
    return positions.reduce((sum, p) => sum + p.minSelection, 0);
  }, [positions]);

  const totalMax = useMemo(() => {
    return positions.reduce((sum, p) => sum + p.maxSelection, 0);
  }, [positions]);

  const positionProgress = useMemo(() => {
    const progress: Record<
      string,
      { selected: number; min: number; max: number }
    > = {};
    positions.forEach((pos) => {
      const selected = selections.get(pos.electionPositionId)?.length ?? 0;
      progress[pos.electionPositionId] = {
        selected,
        min: pos.minSelection,
        max: pos.maxSelection,
      };
    });
    return progress;
  }, [selections, positions]);

  const handleToggle = (
    positionId: string,
    candidateId: string,
    maxSelection: number
  ) => {
    setSelections((prev) => {
      const next = new Map(prev);
      const current = next.get(positionId) ?? [];
      if (current.includes(candidateId)) {
        next.set(
          positionId,
          current.filter((id) => id !== candidateId)
        );
      } else {
        if (current.length >= maxSelection) return prev;
        next.set(positionId, [...current, candidateId]);
      }
      return next;
    });
  };

  const isSelectionValid = useMemo(() => {
    return positions.every((pos) => {
      const selected = selections.get(pos.electionPositionId)?.length ?? 0;
      return selected >= pos.minSelection && selected <= pos.maxSelection;
    });
  }, [selections, positions]);

  const canSubmit = !hasVoted && isSelectionValid;

  const handleSave = () => {
    if (!canSubmit) return;
    setPreviewOpen(true);
  };

  const handleConfirm = () => {
    const result: VoteSelection[] = [];
    selections.forEach((candidateIds, positionId) => {
      if (candidateIds.length > 0) {
        result.push({ positionId, candidateIds });
      }
    });
    onSubmit(result);
    setPreviewOpen(false);
  };

  if (isPending) {
    return <BallotSkeleton />;
  }

  return (
    <div className="mx-auto max-w-4xl space-y-6">
      <div className="flex items-center justify-between">
        <div>
          <h1 className="text-2xl font-bold tracking-tight">{electionName}</h1>
          <p className="text-muted-foreground mt-1">
            Select your preferred candidates for each position.
          </p>
        </div>
        {hasVoted && (
          <Badge variant="secondary" className="gap-1.5 px-3 py-1.5 text-sm">
            <CheckCheck className="h-4 w-4" />
            Already Voted
          </Badge>
        )}
      </div>

      <div className="bg-card rounded-xl border p-4">
        <div className="flex items-center justify-between">
          <div className="flex items-center gap-2">
            <span className="text-sm font-medium">Your Selections</span>
          </div>
          <span className="text-muted-foreground text-sm">
            <span className="text-foreground font-semibold">
              {totalSelected}
            </span>{" "}
            of {totalMax} selected
            {totalRequired > 0 && (
              <span className="text-muted-foreground">
                {" "}
                (minimum {totalRequired})
              </span>
            )}
          </span>
        </div>
        <div className="mt-3 flex gap-1">
          <div className="bg-muted h-2 flex-1 overflow-hidden rounded-full">
            <div
              className="bg-primary h-full rounded-full transition-all duration-500"
              style={{
                width: `${Math.min((totalSelected / totalMax) * 100, 100)}%`,
              }}
            />
          </div>
        </div>
      </div>

      <div className="space-y-8">
        {positions.map((position) => {
          const progress = positionProgress[position.electionPositionId];
          const isComplete = progress.selected >= progress.min;
          const isFull = progress.selected >= progress.max;

          return (
            <section
              key={position.electionPositionId}
              className="bg-card rounded-xl border"
            >
              <div className="border-b p-5">
                <div className="flex items-start justify-between">
                  <div>
                    <h2 className="text-lg font-semibold">
                      {position.positionName}
                    </h2>
                    {position.description && (
                      <p className="text-muted-foreground mt-0.5 text-sm">
                        {position.description}
                      </p>
                    )}
                  </div>
                  <div className="flex shrink-0 items-center gap-1.5">
                    {isComplete ? (
                      <Badge className="bg-green-600 hover:bg-green-600">
                        <CheckCheck className="mr-0.5 h-3 w-3" />
                        {progress.selected}/{progress.min}
                      </Badge>
                    ) : (
                      <Badge variant="outline">
                        {progress.selected}/{progress.min} needed
                      </Badge>
                    )}
                    <span className="text-muted-foreground text-xs">
                      {progress.selected > 0 && isFull
                        ? "Max reached"
                        : `Select up to ${progress.max}`}
                    </span>
                  </div>
                </div>
                <div className="bg-muted mt-2 flex h-1.5 overflow-hidden rounded-full">
                  <div
                    className={`h-full rounded-full transition-all duration-300 ${
                      isComplete ? "bg-green-500" : "bg-primary"
                    }`}
                    style={{
                      width: `${(progress.selected / progress.max) * 100}%`,
                    }}
                  />
                </div>
              </div>

              <div className="p-5">
                <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
                  {position.candidates.map((candidate) => {
                    const isSelected = selections
                      .get(position.electionPositionId)
                      ?.includes(candidate.candidateId);

                    return (
                      <button
                        key={candidate.candidateId}
                        type="button"
                        disabled={hasVoted || (isFull && !isSelected)}
                        onClick={() =>
                          handleToggle(
                            position.electionPositionId,
                            candidate.candidateId,
                            position.maxSelection
                          )
                        }
                        className={`group relative flex items-start gap-3 rounded-xl border p-4 text-left transition-all ${
                          isSelected
                            ? "border-primary bg-primary/5 ring-primary ring-1"
                            : "hover:border-border/80 hover:bg-accent/50 border-border/50"
                        } ${
                          hasVoted || (isFull && !isSelected)
                            ? "pointer-events-none opacity-50"
                            : "cursor-pointer"
                        }`}
                      >
                        {isSelected && (
                          <div className="bg-primary text-primary-foreground absolute top-2 right-2 flex size-5 items-center justify-center rounded-full text-[11px] font-bold">
                            {(selections
                              .get(position.electionPositionId)
                              ?.indexOf(candidate.candidateId) ?? -1) + 1}
                          </div>
                        )}

                        <Avatar size="lg" className="shrink-0">
                          {candidate.profileImage ? (
                            <AvatarImage
                              src={candidate.profileImage}
                              alt={`${candidate.firstName} ${candidate.lastName}`}
                            />
                          ) : (
                            <AvatarFallback>
                              <ImageOff className="size-4" />
                            </AvatarFallback>
                          )}
                        </Avatar>

                        <div className="min-w-0 flex-1">
                          <p className="truncate text-sm font-medium">
                            {candidate.firstName} {candidate.lastName}
                          </p>
                          <p className="text-muted-foreground truncate text-xs">
                            {candidate.email}
                          </p>
                        </div>
                      </button>
                    );
                  })}
                </div>
              </div>
            </section>
          );
        })}
      </div>

      <div className="flex items-center justify-between border-t pt-6">
        <Button variant="outline" onClick={() => window.history.back()}>
          <ChevronLeft className="mr-1 h-4 w-4" />
          Back
        </Button>

        {!hasVoted && (
          <Button
            onClick={handleSave}
            disabled={!canSubmit || isSubmitting}
            loading={isSubmitting}
            rightIcon={<Eye className="h-4 w-4" />}
          >
            <Save className="mr-1 h-4 w-4" />
            Review & Submit
          </Button>
        )}
      </div>

      <PreviewDialog
        open={previewOpen}
        onOpenChange={setPreviewOpen}
        positions={positions}
        selections={selections}
        onConfirm={handleConfirm}
        onEdit={() => setPreviewOpen(false)}
      />
    </div>
  );
}

function BallotSkeleton() {
  return (
    <div className="mx-auto max-w-4xl space-y-6">
      <Skeleton className="h-8 w-64" />
      <Skeleton className="h-4 w-96" />
      <Skeleton className="h-20 w-full rounded-xl" />
      {Array.from({ length: 2 }).map((_, i) => (
        <div key={i} className="bg-card space-y-4 rounded-xl border p-5">
          <Skeleton className="h-6 w-40" />
          <div className="grid gap-3 sm:grid-cols-2 lg:grid-cols-3">
            {Array.from({ length: 3 }).map((_, j) => (
              <Skeleton key={j} className="h-24 w-full rounded-xl" />
            ))}
          </div>
        </div>
      ))}
    </div>
  );
}
