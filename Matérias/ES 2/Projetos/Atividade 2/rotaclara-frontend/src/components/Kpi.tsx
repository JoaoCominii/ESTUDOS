export default function Kpi({
  label,
  value,
  delta,
  tone = "neutral",
}: {
  label: string;
  value: string;
  delta?: string;
  tone?: "up" | "down" | "neutral";
}) {
  return (
    <div className="kpi">
      <div className="label">{label}</div>
      <div className="num">{value}</div>
      {delta && <div className={`delta ${tone}`}>{delta}</div>}
    </div>
  );
}
