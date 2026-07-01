import { ReactNode } from "react";

type Props = {
  title: string;
  actions?: ReactNode;
  children: ReactNode;
};

export function SectionCard({ title, actions, children }: Props) {
  return (
    <div className="bg-card rounded-xl border">
      <div className="flex items-center justify-between border-b p-5">
        <h2 className="text-lg font-semibold">{title}</h2>

        {actions}
      </div>

      <div className="p-5">{children}</div>
    </div>
  );
}
