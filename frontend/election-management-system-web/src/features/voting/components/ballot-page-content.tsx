"use client";

import { useSyncExternalStore } from "react";
import { useRouter } from "next/navigation";
import { useLocalStorage } from "@/hooks/use-local-storage";
import { STORAGE_KEYS } from "@/constants/storage-keys";
import { UserDetails } from "@/features/auth/types/user-details";
import { useVoterElection } from "@/features/voting/hooks/use-voter-election";
import { Ballot } from "@/features/voting/components/ballot";
import { ArrowLeft } from "lucide-react";
import { Button } from "@/components/ui/button";
import type { VoteSelection } from "@/features/voting/types";
import { toast } from "sonner";

function useHydrated() {
  return useSyncExternalStore(
    () => () => {},
    () => true,
    () => false
  );
}

type Props = {
  electionId: string;
};

export function BallotPageContent({ electionId }: Props) {
  const router = useRouter();
  const hydrated = useHydrated();
  const [userData] = useLocalStorage<UserDetails>(
    STORAGE_KEYS.ORGANIZER_USER_DETAILS,
    {} as UserDetails
  );

  const { data, isPending } = useVoterElection(electionId, userData?.userId);

  const handleSubmit = async (selections: VoteSelection[]) => {
    void selections;
    try {
      await new Promise((resolve) => setTimeout(resolve, 1000));
      toast.success("Your vote has been cast successfully!");
      router.push("/dashboard/vote");
    } catch {
      toast.error("Failed to cast vote. Please try again.");
    }
  };

  if (!hydrated) return null;

  if (!userData.userId) {
    return (
      <div className="container mx-auto max-w-4xl py-8">
        <p className="text-muted-foreground">Please log in to continue.</p>
      </div>
    );
  }

  return (
    <div className="container mx-auto py-8">
      <Button
        variant="ghost"
        size="sm"
        onClick={() => router.push("/dashboard/vote")}
        className="mb-4"
      >
        <ArrowLeft className="mr-1 h-4 w-4" />
        Back to Elections
      </Button>

      <Ballot
        electionName={data?.election.name ?? ""}
        positions={data?.positions ?? []}
        isPending={isPending}
        onSubmit={handleSubmit}
        hasVoted={data?.election.hasVoted ?? false}
      />
    </div>
  );
}
