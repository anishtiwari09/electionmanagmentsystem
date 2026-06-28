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

export function CreateElectionDialog() {
  const [open, setOpen] = useState(false);

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

        <form className="space-y-5">
          <div className="space-y-2">
            <Label htmlFor="electionName">Election Name</Label>

            <Input
              id="electionName"
              placeholder="Student Council Election 2026"
            />
          </div>

          <div className="space-y-2">
            <Label htmlFor="startAt">Start Date & Time</Label>

            <Input id="startAt" type="datetime-local" />
          </div>

          <div className="space-y-2">
            <Label htmlFor="endAt">End Date & Time</Label>

            <Input id="endAt" type="datetime-local" />
          </div>

          <DialogFooter>
            <Button
              type="button"
              variant="outline"
              onClick={() => setOpen(false)}
            >
              Cancel
            </Button>

            <Button type="submit">Create Election</Button>
          </DialogFooter>
        </form>
      </DialogContent>
    </Dialog>
  );
}
