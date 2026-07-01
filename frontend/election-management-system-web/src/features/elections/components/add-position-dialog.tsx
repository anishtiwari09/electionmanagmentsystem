"use client";

import { useState } from "react";
import { useFieldArray, useForm, FormProvider } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Plus } from "lucide-react";

import { Button } from "@/components/ui/button";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";

import { useCreatePositions } from "../hooks/use-create-positions";
import { PositionFormRow } from "@/features/election/components/position-form-row";
import {
  createElectionPositionSchema,
  type CreateElectionPositionFormData,
} from "@/features/election/schemas/create-election-position-schema";
import { useQueryClient } from "@tanstack/react-query";
import { queryKeys } from "@/api/query-keys";

interface Props {
  userId: string;
  electionId: string;
}

export function AddPositionDialog({ userId, electionId }: Props) {
  const [open, setOpen] = useState(false);
  const queryClient = useQueryClient();
  const form = useForm<CreateElectionPositionFormData>({
    resolver: zodResolver(createElectionPositionSchema),
    mode: "onChange",
    reValidateMode: "onChange",
    criteriaMode: "all",
    shouldFocusError: true,
    defaultValues: {
      positions: [
        {
          positionName: "",
          description: "",
          minSelection: 0,
          maxSelection: 1,
        },
      ],
    },
  });

  const { fields, append, remove } = useFieldArray({
    control: form.control,
    name: "positions",
  });

  const mutation = useCreatePositions({
    userId,
    electionId,
    onSuccess: () => {
      queryClient.refetchQueries({
        queryKey: queryKeys.elections.details(electionId),
      });
      setOpen(false);

      form.reset();
    },
  });

  const onSubmit = (values: CreateElectionPositionFormData) => {
    mutation.mutate(values);
  };

  const addPosition = () => {
    append({
      positionName: "",
      description: "",
      minSelection: 1,
      maxSelection: 1,
    });
  };

  return (
    <Dialog
      open={open}
      onOpenChange={(v) => {
        if (!v && mutation.isPending) return;
        setOpen(v);
      }}
    >
      <DialogTrigger asChild>
        <Button>
          <Plus className="mr-2 h-4 w-4" />
          Add Positions
        </Button>
      </DialogTrigger>

      <DialogContent className="max-h-[90vh] overflow-y-auto sm:max-w-4xl">
        <DialogHeader>
          <DialogTitle>Add Election Positions</DialogTitle>

          <DialogDescription>
            Create one or more positions. Each must have a unique name.
          </DialogDescription>
        </DialogHeader>

        <FormProvider {...form}>
          <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-6">
            <div className="space-y-4">
              {fields.map((field, index) => (
                <PositionFormRow
                  key={field.id}
                  index={index}
                  totalRows={fields.length}
                  remove={remove}
                />
              ))}
            </div>

            <div className="flex items-center justify-between">
              <Button
                type="button"
                variant="outline"
                onClick={addPosition}
                disabled={mutation.isPending}
              >
                <Plus className="mr-2 h-4 w-4" />
                Add Position
              </Button>

              <Button type="submit" disabled={mutation.isPending}>
                {mutation.isPending ? "Saving..." : "Save Positions"}
              </Button>
            </div>
          </form>
        </FormProvider>
      </DialogContent>
    </Dialog>
  );
}
