import { useEffect, useMemo, useState } from "react";
import { horaFormatada, tempoParadoFormatado } from "../data/mock";
import { calcularTempoParadoPonto } from "../domain/regras";
import { api } from "../api/client";
import type { Motorista, Roteiro } from "../types/domain";

interface Linha {
  data: string;
  motorista: string;
  ponto: number;
  endereco: string;
  chegada: string;
  saida: string;
  tempoParado: string;
}

function montarLinhas(roteiros: Roteiro[], motoristas: Motorista[]): Linha[] {
  const linhas: Linha[] = [];
  for (const roteiro of roteiros) {
    for (const ponto of roteiro.pontos) {
      if (ponto.ordemNoRoteiro === 1) continue; // partida não entra no histórico de paradas
      linhas.push({
        data: new Date(roteiro.data).toLocaleDateString("pt-BR"),
        motorista: motoristas.find((m) => m.id === roteiro.motoristaId)?.nome ?? "—",
        ponto: ponto.ordemNoRoteiro,
        endereco: ponto.endereco,
        chegada: horaFormatada(ponto.dataHoraChegada),
        saida: horaFormatada(ponto.dataHoraSaida),
        tempoParado: tempoParadoFormatado(calcularTempoParadoPonto(ponto)),
      });
    }
  }
  return linhas;
}

export default function Historico() {
  const [motoristaId, setMotoristaId] = useState<string>("todos");
  const [inicio, setInicio] = useState("2026-09-15");
  const [fim, setFim] = useState(new Date().toISOString().slice(0, 10));
  const [motoristas, setMotoristas] = useState<Motorista[]>([]);
  const [roteiros, setRoteiros] = useState<Roteiro[]>([]);
  const [erro, setErro] = useState("");

  useEffect(() => {
    Promise.all([api.listarMotoristas(), api.listarRoteiros(inicio, fim)])
      .then(([ms, rs]) => { setMotoristas(ms); setRoteiros(rs); })
      .catch((e) => setErro(e.message));
  }, [inicio, fim]);

  const linhas = useMemo(() => montarLinhas(roteiros, motoristas), [roteiros, motoristas]);

  const filtradas =
    motoristaId === "todos"
      ? linhas
    : linhas.filter((l) => l.motorista === motoristas.find((m) => m.id === motoristaId)?.nome);

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Histórico de pontos e tempos parados</h1>
          <div className="meta">
            Consulta por período, sempre associada ao endereço registrado
          </div>
        </div>
      </div>
      {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}

      <div className="panel" style={{ marginBottom: 18 }}>
        <div className="field-row3">
          <div className="field">
            <label>De</label>
            <input type="date" value={inicio} onChange={(e) => setInicio(e.target.value)} />
          </div>
          <div className="field">
            <label>Até</label>
            <input type="date" value={fim} onChange={(e) => setFim(e.target.value)} />
          </div>
          <div className="field">
            <label>Motorista</label>
            <select
              value={motoristaId}
              onChange={(e) => setMotoristaId(e.target.value)}
            >
              <option value="todos">Todos os motoristas</option>
              {motoristas.map((m) => (
                <option key={m.id} value={m.id}>
                  {m.nome}
                </option>
              ))}
            </select>
          </div>
        </div>
      </div>

      <div className="table-card">
        <div className="table-card-head">
          <h3>{filtradas.length} registros encontrados</h3>
        </div>
        <table>
          <thead>
            <tr>
              <th>Data</th>
              <th>Motorista</th>
              <th>Ponto</th>
              <th>Endereço</th>
              <th className="num">Chegada</th>
              <th className="num">Saída</th>
              <th className="num">Tempo parado</th>
            </tr>
          </thead>
          <tbody>
            {filtradas.map((l, i) => (
              <tr key={i}>
                <td>{l.data}</td>
                <td>{l.motorista}</td>
                <td>{l.ponto}</td>
                <td>{l.endereco}</td>
                <td className="num">{l.chegada}</td>
                <td className="num">{l.saida}</td>
                <td className="num">{l.tempoParado}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  );
}
