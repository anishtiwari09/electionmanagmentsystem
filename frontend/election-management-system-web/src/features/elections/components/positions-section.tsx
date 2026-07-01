"use client";

import { SectionCard } from "./section-card";
import { ElectionPosition } from "../types";
import { AddPositionDialog } from "./add-position-dialog";

type Props = {
  positions: ElectionPosition[];
  electionId: string;
  userId: string;
};

export function PositionsSection({ positions, electionId, userId }: Props) {
  return (
    <SectionCard
      title="Election Positions"
      actions={<AddPositionDialog electionId={electionId} userId={userId} />}
    >
      <div className="grid gap-4 md:grid-cols-2">
        {positions.map((position) => (
          <div
            key={position.electionPositionId}
            className="rounded-lg border p-4"
          >
            <div className="flex justify-between">
              <h3 className="font-semibold">{position.positionName}</h3>

              <span className="text-muted-foreground text-sm">
                {position.minSelection} - {position.maxSelection}
              </span>
            </div>

            <p className="text-muted-foreground mt-2 text-sm">
              {position.description}
            </p>
          </div>
        ))}
      </div>
    </SectionCard>
  );
}
