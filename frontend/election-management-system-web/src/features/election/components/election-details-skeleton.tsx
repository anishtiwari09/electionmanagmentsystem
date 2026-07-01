"use client";

import { Skeleton } from "@/components/ui/skeleton";

export function ElectionDetailsSkeleton() {
  return (
    <div className="mx-auto flex w-full max-w-7xl flex-col gap-6">
      <Skeleton className="h-10 w-40" />

      <Skeleton className="h-56 w-full" />

      <Skeleton className="h-72 w-full" />

      <Skeleton className="h-72 w-full" />

      <Skeleton className="h-72 w-full" />
    </div>
  );
}
