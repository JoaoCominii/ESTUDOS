import { useEffect, useState } from "react";
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
import { tempoParadoFormatado } from "../data/mock";
import { api } from "../api/client";
import type { Motorista, Parametro, Roteiro } from "../types/domain";
import type { StatusRoteiro } from "../types/domain";

type Recorte = "dia" | "mes" | "periodo";

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
  const hoje = new Date().toISOString().slice(0, 10);
  const [roteiros, setRoteiros] = useState<Roteiro[]>([]);
  const [motoristas, setMotoristas] = useState<Motorista[]>([]);
  const [parametro, setParametro] = useState<Parametro>({ valorCombustivelPorLitro: 0, custoPorKm: 0, jornadaPadraoHoras: 8, limiteAlertaParadaMinutos: 30 });
  const [dashboard, setDashboard] = useState<Awaited<ReturnType<typeof api.dashboard>> | null>(null);
  const [erro, setErro] = useState("");

  useEffect(() => {
    Promise.all([api.dashboard(hoje, hoje), api.listarRoteiros(hoje, hoje), api.listarMotoristas(), api.buscarParametro()])
      .then(([d, rs, ms, p]) => { setDashboard(d); setRoteiros(rs); setMotoristas(ms); setParametro(p); })
      .catch((e) => setErro(e.message));
  }, [hoje]);

  const roteiroDestaque = roteiros[0];
  const tempoParadoTotal = dashboard?.kpis.tempoParadoTotalMinutos ?? 0;
  const custoTotal = dashboard?.kpis.custoEstimadoTotal ?? 0;
  const concluidos = dashboard?.kpis.roteirosConcluidos ?? 0;
  const tempoPorDia = dashboard?.tempoParadoPorDia.map((p) => ({ dia: new Date(`${p.data}T12:00:00`).toLocaleDateString("pt-BR", { weekday: "short" }), minutos: p.minutos })) ?? [];

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

      {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}
      {roteiroDestaque && <div className="timeline-card">
        <div className="timeline-head">
          <h3>
            Roteiro em destaque — {motoristas.find((m) => m.id === roteiroDestaque.motoristaId)?.nome ?? "—"}{" "}
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
      </div>}

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
          value={`${concluidos} / ${dashboard?.kpis.roteirosTotal ?? 0}`}
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
            {(dashboard?.roteiros ?? []).map((r) => (
              <tr key={r.id}>
                <td>{r.motoristaNome}</td>
                <td>{r.quantidadePontos || "—"}</td>
                <td className="num">
                  {tempoParadoFormatado(r.tempoTotalParadoMinutos)}
                </td>
                <td className="num">
                  R$ {r.custoEstimado.toFixed(2).replace(".", ",")}
                </td>
                <td>
                  <Pill tone={statusTone(r.status.toLowerCase() as StatusRoteiro)}>
                    {statusLabel(r.status.toLowerCase() as StatusRoteiro)}
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
