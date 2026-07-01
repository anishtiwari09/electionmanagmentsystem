import { ElectionDetails } from "@/features/elections/components/election-details";

type Props = {
  params: Promise<{
    electionId: string;
  }>;
};

export default async function ElectionPage({ params }: Props) {
  const { electionId: id } = await params;

  return <ElectionDetails electionId={id} />;
}
