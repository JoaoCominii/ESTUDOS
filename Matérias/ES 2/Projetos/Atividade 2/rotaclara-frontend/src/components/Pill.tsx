import type { ReactNode } from "react";

export type PillTone = "ok" | "warn" | "bad";

export default function Pill({
  tone,
  children,
}: {
  tone: PillTone;
  children: ReactNode;
}) {
  return <span className={`pill ${tone}`}>{children}</span>;
}
