"use client";

import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
  DialogDescription,
  DialogFooter,
} from "@/components/ui/dialog";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar";
import { ImageOff, Edit, Send, CheckCircle2 } from "lucide-react";
import type { VoterPosition } from "../types";

type Props = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  positions: VoterPosition[];
  selections: Map<string, string[]>;
  onConfirm: () => void;
  onEdit: () => void;
};

export function PreviewDialog({
  open,
  onOpenChange,
  positions,
  selections,
  onConfirm,
  onEdit,
}: Props) {
  const getCandidate = (positionId: string, candidateId: string) => {
    const position = positions.find((p) => p.electionPositionId === positionId);
    return position?.candidates.find((c) => c.candidateId === candidateId);
  };

  const totalSelected = Array.from(selections.values()).reduce(
    (sum, ids) => sum + ids.length,
    0
  );

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>Review Your Vote</DialogTitle>
          <DialogDescription>
            Please review your selections before casting your vote. You can edit
            if needed.
          </DialogDescription>
        </DialogHeader>

        <div className="space-y-4">
          <div className="bg-primary/5 flex items-center gap-2 rounded-lg p-3 text-sm">
            <CheckCircle2 className="text-primary h-4 w-4" />
            <span>
              You have selected <strong>{totalSelected}</strong> candidate
              {totalSelected !== 1 ? "s" : ""} across {positions.length}{" "}
              position{positions.length !== 1 ? "s" : ""}.
            </span>
          </div>

          <div className="space-y-3">
            {positions.map((position) => {
              const selectedIds = selections.get(position.electionPositionId);
              if (!selectedIds || selectedIds.length === 0) return null;

              return (
                <div key={position.electionPositionId}>
                  <h4 className="mb-2 text-sm font-medium">
                    {position.positionName}
                  </h4>
                  <div className="space-y-2">
                    {selectedIds.map((candidateId, index) => {
                      const candidate = getCandidate(
                        position.electionPositionId,
                        candidateId
                      );
                      if (!candidate) return null;

                      return (
                        <div
                          key={candidateId}
                          className="bg-muted/50 flex items-center gap-3 rounded-lg p-3"
                        >
                          <span className="bg-primary text-primary-foreground flex size-5 shrink-0 items-center justify-center rounded-full text-[11px] font-bold">
                            {index + 1}
                          </span>
                          <Avatar size="sm">
                            {candidate.profileImage ? (
                              <AvatarImage
                                src={candidate.profileImage}
                                alt={`${candidate.firstName} ${candidate.lastName}`}
                              />
                            ) : (
                              <AvatarFallback>
                                <ImageOff className="size-3" />
                              </AvatarFallback>
                            )}
                          </Avatar>
                          <div className="min-w-0 flex-1">
                            <p className="text-sm font-medium">
                              {candidate.firstName} {candidate.lastName}
                            </p>
                            <p className="text-muted-foreground truncate text-xs">
                              {candidate.email}
                            </p>
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              );
            })}
          </div>
        </div>

        <Separator />

        <DialogFooter>
          <Button
            variant="outline"
            onClick={onEdit}
            leftIcon={<Edit className="h-4 w-4" />}
          >
            Edit Selections
          </Button>
          <Button onClick={onConfirm} rightIcon={<Send className="h-4 w-4" />}>
            Confirm & Cast Vote
          </Button>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
}
