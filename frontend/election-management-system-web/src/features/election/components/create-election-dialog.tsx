"use client";

import { useState } from "react";
import { Plus } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { CreateElectionRequest } from "../types/create-election-request";
import {
  QueryClient,
  useMutation,
  useQueryClient,
} from "@tanstack/react-query";
import { electionApi } from "../api/election-api";
import { toast } from "sonner";
import { GetElectionsRequest } from "../types/get-elections-request";
import { queryKeys } from "@/api/query-keys";
import { DateTimePicker } from "@/components/ui/date-time-picker";
import { Textarea } from "@/components/ui/textarea";

export function CreateElectionDialog({
  userId,
  filters,
}: {
  userId: string;
  filters: GetElectionsRequest;
}) {
  const [open, setOpen] = useState(false);
  const queryClient = useQueryClient();
  const [startAt, setStartAt] = useState<Date>();
  const [endAt, setEndAt] = useState<Date>();
  const createElectionMutation = useMutation({
    mutationFn: (request: CreateElectionRequest) =>
      electionApi.create(userId, request),

    onSuccess: async () => {
      await queryClient.refetchQueries({
        queryKey: queryKeys.elections.list(filters),
      });

      toast.success("Election created successfully.");

      setOpen(false);
    },

    onError: (error) => {
      toast.error(error.message);
    },
  });
  const handleSubmit = (e: React.FormEvent<HTMLFormElement>) => {
    e.preventDefault();

    const form = new FormData(e.currentTarget);
    createElectionMutation.mutate({
      electionName: form.get("electionName")?.toString() ?? "",
      startAt: startAt?.toISOString(),
      endAt: endAt?.toISOString(),
      description: form.get("descriptionName")?.toString() ?? "",
    });
  };
  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button>
          <Plus className="mr-2 h-4 w-4" />
          Create Election
        </Button>
      </DialogTrigger>

      <DialogContent className="sm:max-w-lg">
        <DialogHeader>
          <DialogTitle>Create Election</DialogTitle>

          <DialogDescription>Create a new election.</DialogDescription>
        </DialogHeader>

        <form className="space-y-5" onSubmit={handleSubmit}>
          <div className="space-y-2">
            <Label htmlFor="electionName">Election Name</Label>

            <Input
              id="electionName"
              name="electionName"
              placeholder="Student Council Election 2026"
              // type="text"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="electionName">Election Description</Label>

            <Textarea
              id="descriptionName"
              name="descriptionName"
              placeholder="Enter Election Description..."
              // type="text"
            />
          </div>
          <div className="space-y-2">
            <Label htmlFor="startAt">Start Date & Time</Label>

            <DateTimePicker value={startAt} onChange={setStartAt} />
          </div>

          <div className="space-y-2">
            <Label htmlFor="endAt">End Date & Time</Label>

            <DateTimePicker value={endAt} onChange={setEndAt} />
          </div>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => setOpen(false)}
              disabled={createElectionMutation.isPending}
            >
              Cancel
            </Button>

            <Button
              type="submit"
              loading={createElectionMutation.isPending}
              disabled={createElectionMutation.isPending}
            >
              Create Election
            </Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
