"use client";

import { Trash2 } from "lucide-react";
import { useFormContext } from "react-hook-form";

import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { Textarea } from "@/components/ui/textarea";

import type { CreateElectionPositionFormData } from "../schemas/create-election-position-schema";

interface PositionFormRowProps {
  index: number;
  totalRows: number;
  remove: (index: number) => void;
}

export function PositionFormRow({
  index,
  totalRows,
  remove,
}: PositionFormRowProps) {
  const {
    register,
    watch,
    formState: { errors, dirtyFields },
  } = useFormContext<CreateElectionPositionFormData>();

  const min = watch(`positions.${index}.minSelection`);
  const max = watch(`positions.${index}.maxSelection`);

  const safeMin = Number.isFinite(min) ? min : 0;
  const safeMax = Number.isFinite(max) ? max : 0;

  const positionError =
    dirtyFields.positions?.[index]?.positionName &&
    errors.positions?.[index]?.positionName;

  const minError =
    dirtyFields.positions?.[index]?.minSelection &&
    errors.positions?.[index]?.minSelection;

  const maxError =
    dirtyFields.positions?.[index]?.maxSelection &&
    errors.positions?.[index]?.maxSelection;

  return (
    <div className="bg-background space-y-4 rounded-lg border p-4">
      {/* Header */}
      <div className="flex items-center justify-between">
        <p className="text-sm font-semibold">Position #{index + 1}</p>

        {totalRows > 1 && (
          <Button
            type="button"
            variant="destructive"
            size="sm"
            onClick={() => remove(index)}
          >
            <Trash2 className="mr-2 h-4 w-4" />
            Remove
          </Button>
        )}
      </div>

      {/* Position Name */}
      <div className="space-y-1">
        <label className="text-sm font-medium">Position Name</label>

        <Input
          placeholder="e.g. President"
          {...register(`positions.${index}.positionName` as const)}
        />

        {positionError && (
          <p className="text-destructive text-sm">
            {errors.positions?.[index]?.positionName?.message}
          </p>
        )}
      </div>

      {/* Description */}
      <div className="space-y-1">
        <label className="text-sm font-medium">Description</label>

        <Textarea
          placeholder="Optional"
          {...register(`positions.${index}.description` as const)}
        />

        {dirtyFields.positions?.[index]?.description &&
          errors.positions?.[index]?.description && (
            <p className="text-destructive text-sm">
              {errors.positions[index]?.description?.message}
            </p>
          )}
      </div>

      {/* Selection Limit */}
      <div className="space-y-2">
        <label className="text-sm font-medium">Selection Limit</label>

        <div className="bg-muted/40 rounded-md border px-3 py-2 text-sm">
          Voter can select: <span className="font-medium">{safeMin}</span> to{" "}
          <span className="font-medium">{safeMax}</span> candidates
        </div>

        <div className="grid grid-cols-2 gap-4">
          {/* Min */}
          <div className="space-y-1">
            <label className="text-muted-foreground text-xs">Min</label>

            <Input
              type="number"
              min={0}
              {...register(`positions.${index}.minSelection` as const, {
                valueAsNumber: true,
              })}
            />

            {minError && (
              <p className="text-destructive text-sm">
                {errors.positions?.[index]?.minSelection?.message}
              </p>
            )}
          </div>

          {/* Max */}
          <div className="space-y-1">
            <label className="text-muted-foreground text-xs">Max</label>

            <Input
              type="number"
              min={0}
              {...register(`positions.${index}.maxSelection` as const, {
                valueAsNumber: true,
              })}
            />

            {maxError && (
              <p className="text-destructive text-sm">
                {errors.positions?.[index]?.maxSelection?.message}
              </p>
            )}
          </div>
        </div>
      </div>
    </div>
  );
}
