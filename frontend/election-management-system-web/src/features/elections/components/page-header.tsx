import { CalendarDays } from "lucide-react";

type Props = {
  title: string;
  description: string;
  startDate: string;
  endDate: string;
};

export function PageHeader({ title, description, startDate, endDate }: Props) {
  return (
    <div className="bg-card space-y-3 rounded-xl border p-6">
      <div>
        <h1 className="text-3xl font-bold">{title}</h1>

        <p className="text-muted-foreground mt-2">{description}</p>
      </div>

      <div className="text-muted-foreground flex items-center gap-2 text-sm">
        <CalendarDays className="h-4 w-4" />
        {startDate} → {endDate}
      </div>
    </div>
  );
}
