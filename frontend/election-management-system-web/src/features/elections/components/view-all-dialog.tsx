"use client";

import { useState, useEffect, useRef, ReactNode } from "react";
import {
  Dialog,
  DialogContent,
  DialogHeader,
  DialogTitle,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Search, Loader2 } from "lucide-react";

const BATCH_SIZE = 20;

type Props = {
  open: boolean;
  onOpenChange: (open: boolean) => void;
  title: string;
  totalCount: number;
  searchPlaceholder?: string;
  searchValue?: string;
  onSearchChange?: (value: string) => void;
  children: (displayCount: number) => ReactNode;
};

export function ViewAllDialog({
  open,
  onOpenChange,
  title,
  totalCount,
  searchPlaceholder = "Search...",
  searchValue,
  onSearchChange,
  children,
}: Props) {
  const [displayCount, setDisplayCount] = useState(() => {
    return BATCH_SIZE;
  });
  const sentinelRef = useRef<HTMLDivElement>(null);
  const scrollRef = useRef<HTMLDivElement>(null);

  useEffect(() => {
    if (open) {
      scrollRef.current?.scrollTo(0, 0);
    }
  }, [open]);

  useEffect(() => {
    const sentinel = sentinelRef.current;
    if (!sentinel) return;

    const observer = new IntersectionObserver(
      (entries) => {
        if (entries[0].isIntersecting && displayCount < totalCount) {
          setDisplayCount((prev) => Math.min(prev + BATCH_SIZE, totalCount));
        }
      },
      { threshold: 0.1 }
    );

    observer.observe(sentinel);
    return () => observer.disconnect();
  }, [displayCount, totalCount]);

  const hasMore = displayCount < totalCount;

  return (
    <Dialog open={open} onOpenChange={onOpenChange}>
      <DialogContent className="flex max-h-[85vh] flex-col gap-0 p-0 sm:max-w-3xl">
        <DialogHeader className="shrink-0 border-b px-6 pt-5 pb-3">
          <div className="flex items-center justify-between">
            <DialogTitle className="text-lg">
              {title}
              <span className="text-muted-foreground ml-2 text-sm font-normal">
                ({totalCount})
              </span>
            </DialogTitle>
          </div>
          {onSearchChange && (
            <div className="relative mt-3">
              <Search className="text-muted-foreground absolute top-1/2 left-3 h-4 w-4 -translate-y-1/2" />
              <Input
                placeholder={searchPlaceholder}
                value={searchValue}
                onChange={(e) => onSearchChange(e.target.value)}
                className="h-9 pl-9"
              />
            </div>
          )}
        </DialogHeader>

        <div ref={scrollRef} className="flex-1 overflow-y-auto px-0">
          <div className="px-6">{children(displayCount)}</div>

          {hasMore ? (
            <>
              <div ref={sentinelRef} className="h-4" />
              <div className="text-muted-foreground flex items-center justify-center py-6 text-sm">
                <Loader2 className="mr-2 h-4 w-4 animate-spin" />
                Loading more...
              </div>
            </>
          ) : null}

          {!hasMore && totalCount > 0 && (
            <div className="text-muted-foreground mx-6 border-t py-6 text-center text-sm">
              Showing all {totalCount} items
            </div>
          )}

          {totalCount === 0 && (
            <div className="flex flex-col items-center justify-center py-12 text-center">
              <div className="text-muted-foreground/30 mb-3 text-4xl">∅</div>
              <p className="text-muted-foreground text-sm">No items found</p>
            </div>
          )}
        </div>
      </DialogContent>
    </Dialog>
  );
}
