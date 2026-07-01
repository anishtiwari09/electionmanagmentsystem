import { ReactNode } from "react";
import { Inbox } from "lucide-react";

type Props = {
  title: string;
  description: string;
  action?: ReactNode;
};

export function EmptyState({ title, description, action }: Props) {
  return (
    <div className="flex flex-col items-center justify-center rounded-xl border border-dashed py-16 text-center">
      <Inbox className="text-muted-foreground mb-4 h-12 w-12" />

      <h3 className="text-lg font-semibold">{title}</h3>

      <p className="text-muted-foreground mt-2 max-w-md text-sm">
        {description}
      </p>

      {action && <div className="mt-6">{action}</div>}
    </div>
  );
}
