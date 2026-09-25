import { useState } from "react";
import {
  BarChart,
  Bar,
  XAxis,
  ResponsiveContainer,
  Tooltip,
} from "recharts";
import Kpi from "../components/Kpi";
import Pill from "../components/Pill";
import RouteStrip from "../components/RouteStrip";
import { motoristas, nomeMotorista, parametro, roteiros, tempoParadoFormatado } from "../data/mock";
import type { StatusRoteiro } from "../types/domain";

type Recorte = "dia" | "mes" | "periodo";

const tempoPorDia = [
  { dia: "Seg", minutos: 38 },
  { dia: "Ter", minutos: 52 },
  { dia: "Qua", minutos: 20 },
  { dia: "Qui", minutos: 65 },
  { dia: "Sex", minutos: 44 },
  { dia: "Sáb", minutos: 15 },
  { dia: "Dom", minutos: 5 },
];

function statusTone(status: StatusRoteiro): "ok" | "warn" | "bad" {
  if (status === "concluido") return "ok";
  if (status === "em_andamento") return "warn";
  return "bad";
}

function statusLabel(status: StatusRoteiro): string {
  if (status === "concluido") return "Concluído";
  if (status === "em_andamento") return "Em andamento";
  return "Planejado";
}

export default function Painel() {
  const [recorte, setRecorte] = useState<Recorte>("dia");
  const roteiroDestaque = roteiros[0];

  const tempoParadoTotal = roteiros.reduce(
    (t, r) => t + r.tempoTotalParadoMinutos,
    0
  );
  const custoTotal = roteiros.reduce((t, r) => t + r.custoEstimado, 0);
  const concluidos = roteiros.filter((r) => r.status === "concluido").length;

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Painel de tempo parado</h1>
          <div className="meta">
            Dados consolidados de todos os motoristas · atualizado às 14:32
          </div>
        </div>
        <div className="segmented">
          {(["dia", "mes", "periodo"] as Recorte[]).map((r) => (
            <button
              key={r}
              className={recorte === r ? "active" : ""}
              onClick={() => setRecorte(r)}
            >
              {r === "dia" ? "Dia" : r === "mes" ? "Mês" : "Período"}
            </button>
          ))}
        </div>
      </div>

      <div className="timeline-card">
        <div className="timeline-head">
          <h3>
            Roteiro em destaque — {nomeMotorista(roteiroDestaque.motoristaId)}{" "}
            · hoje
          </h3>
          <span className="sub">
            {roteiroDestaque.pontos.length} pontos ·{" "}
            {tempoParadoFormatado(roteiroDestaque.tempoTotalParadoMinutos)}{" "}
            parado no total
          </span>
        </div>
        <RouteStrip roteiro={roteiroDestaque} parametro={parametro} />
        <div className="route-labels">
          <span>Início do roteiro</span>
          <span>Ponto final</span>
        </div>
        <div className="legend">
          <span>
            <i className="sw" style={{ background: "#EAEDEC" }} />
            Partida — não conta tempo (RN01)
          </span>
          <span>
            <i className="sw" style={{ background: "var(--amber)" }} />
            Parada dentro do esperado
          </span>
          <span>
            <i className="sw" style={{ background: "var(--rust)" }} />
            Parada acima de {parametro.limiteAlertaParadaMinutos} min
          </span>
        </div>
      </div>

      <div className="kpi-row">
        <Kpi
          label="Tempo parado total (dia)"
          value={tempoParadoFormatado(tempoParadoTotal)}
          delta="↑ 12% vs. ontem"
          tone="up"
        />
        <Kpi
          label="Custo estimado (dia)"
          value={`R$ ${custoTotal.toFixed(2).replace(".", ",")}`}
          delta="↓ 4% vs. ontem"
          tone="down"
        />
        <Kpi
          label="Roteiros concluídos"
          value={`${concluidos} / ${roteiros.length}`}
          delta={`${roteiros.length - concluidos} em andamento`}
        />
        <Kpi
          label="Média parada / roteiro"
          value={tempoParadoFormatado(
            Math.round(tempoParadoTotal / roteiros.length)
          )}
          delta={`jornada padrão ${parametro.jornadaPadraoHoras}h`}
        />
      </div>

      <div className="grid-2">
        <div className="panel">
          <h3>Tempo parado por dia — últimos 7 dias</h3>
          <ResponsiveContainer width="100%" height={150}>
            <BarChart data={tempoPorDia}>
              <XAxis
                dataKey="dia"
                axisLine={false}
                tickLine={false}
                tick={{ fontSize: 11, fill: "var(--text-muted)" }}
              />
              <Tooltip
                formatter={(v: number) => [`${v} min`, "Tempo parado"]}
                labelStyle={{ fontSize: 12 }}
              />
              <Bar dataKey="minutos" radius={[4, 4, 0, 0]} fill="#DE8B22" />
            </BarChart>
          </ResponsiveContainer>
        </div>

        <div className="panel">
          <h3>Frota cadastrada</h3>
          <table>
            <thead>
              <tr>
                <th>Motorista</th>
                <th>Veículo</th>
                <th className="num">Km/l</th>
              </tr>
            </thead>
            <tbody>
              {motoristas.map((m) => (
                <tr key={m.id}>
                  <td>{m.nome}</td>
                  <td>{m.veiculo}</td>
                  <td className="num">{m.rendimentoKmLitro}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>

      <div className="table-card">
        <div className="table-card-head">
          <h3>Roteiros do dia</h3>
          <button className="btn ghost">Exportar relatório do período →</button>
        </div>
        <table>
          <thead>
            <tr>
              <th>Motorista</th>
              <th>Pontos</th>
              <th className="num">Tempo parado</th>
              <th className="num">Custo estimado</th>
              <th>Status</th>
            </tr>
          </thead>
          <tbody>
            {roteiros.map((r) => (
              <tr key={r.id}>
                <td>{nomeMotorista(r.motoristaId)}</td>
                <td>{r.pontos.length || "—"}</td>
                <td className="num">
                  {tempoParadoFormatado(r.tempoTotalParadoMinutos)}
                </td>
                <td className="num">
                  R$ {r.custoEstimado.toFixed(2).replace(".", ",")}
                </td>
                <td>
                  <Pill tone={statusTone(r.status)}>
                    {statusLabel(r.status)}
                  </Pill>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
