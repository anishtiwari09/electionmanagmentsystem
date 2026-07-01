"use client";

import { Button } from "@/components/ui/button";
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
import { ElectionVoter } from "../types";
import { MoreHorizontal, Plus, Trash2, Upload } from "lucide-react";

type Props = {
  voters: ElectionVoter[];
};

export function VotersSection({ voters }: Props) {
  if (voters.length === 0) {
    return (
      <SectionCard title="Election Voters">
        <EmptyState
          title="No voters found"
          description="Upload voters in bulk to get started."
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

  return (
    <SectionCard
      title="Election Voters"
      actions={
        <div className="flex gap-2">
          <Button variant="outline">
            <Upload className="mr-2 h-4 w-4" />
            Bulk Upload
          </Button>

          <Button>
            <Plus className="mr-2 h-4 w-4" />
            Add Voter
          </Button>
        </div>
      }
    >
      <Table>
        <TableHeader>
          <TableRow>
            <TableHead>Name</TableHead>
            <TableHead>Email</TableHead>
            <TableHead className="w-30 text-right">Actions</TableHead>
          </TableRow>
        </TableHeader>

        <TableBody>
          {voters.map((voter) => (
            <TableRow key={voter.id}>
              <TableCell className="font-medium">{voter.fullName}</TableCell>

              <TableCell>{voter.email}</TableCell>

              <TableCell className="text-right">
                <DropdownMenu>
                  <DropdownMenuTrigger asChild>
                    <Button variant="ghost" size="icon">
                      <MoreHorizontal className="h-4 w-4" />
                    </Button>
                  </DropdownMenuTrigger>

                  <DropdownMenuContent align="end">
                    <DropdownMenuItem className="text-destructive focus:text-destructive">
                      <Trash2 className="mr-2 h-4 w-4" />
                      Remove Voter
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
