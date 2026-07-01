"use client";

import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import {
  DropdownMenu,
  DropdownMenuContent,
  DropdownMenuItem,
  DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu";
import { EmptyState } from "./empty-state";
import { SectionCard } from "./section-card";
import { ElectionCandidate, ElectionPosition } from "../types";
import {
  MoreHorizontal,
  Upload,
  Plus,
  Trash2,
  ArrowRightLeft,
} from "lucide-react";

type Props = {
  candidates: ElectionCandidate[];
  positions: ElectionPosition[];
};

export function CandidatesSection({ candidates, positions }: Props) {
  if (candidates.length === 0) {
    return (
      <SectionCard title="Election Candidates">
        <EmptyState
          title="No candidates found"
          description="Upload candidates in bulk to get started."
          action={
            <Button>
              <Upload className="mr-2 h-4 w-4" />
              Bulk Upload
            </Button>
          }
        />
      </SectionCard>
    );
  }

  const getPositionName = (positionId: string) =>
    positions.find((p) => p.electionPositionId === positionId)?.positionName ??
    "-";

  return (
    <SectionCard
      title="Election Candidates"
      actions={
        <div className="flex gap-2">
          <Button variant="outline">
            <Upload className="mr-2 h-4 w-4" />
            Bulk Upload
          </Button>

          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Add Candidate
          </Button>
        </div>
      }
    >
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead>Position</TableHead>
            <TableHead className="w-30 text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {candidates.map((candidate) => (
            <TableRow key={candidate.id}>
              <TableCell className="font-medium">
                {candidate.fullName}
              </TableCell>

              <TableCell>{candidate.email}</TableCell>

              <TableCell>
                <Badge variant="secondary">
                  {getPositionName(candidate.positionId)}
                </Badge>
              </TableCell>

              <TableCell className="text-right">
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon">
                      <MoreHorizontal className="h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>

                  <DropdownMenuContent align="end">
                    <DropdownMenuItem>
                      <ArrowRightLeft className="mr-2 h-4 w-4" />
                      Change Position
                    </DropdownMenuItem>

                    <DropdownMenuItem className="text-destructive focus:text-destructive">
                      <Trash2 className="mr-2 h-4 w-4" />
                      Remove Candidate
                    </DropdownMenuItem>
                  </DropdownMenuContent>
                </DropdownMenu>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </SectionCard>
  );
}
