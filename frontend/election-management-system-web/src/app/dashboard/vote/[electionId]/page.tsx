import { BallotPageContent } from "@/features/voting/components/ballot-page-content";

type Props = {
  params: Promise<{
    electionId: string;
  }>;
};

export default async function VotePage({ params }: Props) {
  const { electionId } = await params;

  return <BallotPageContent electionId={electionId} />;
}
