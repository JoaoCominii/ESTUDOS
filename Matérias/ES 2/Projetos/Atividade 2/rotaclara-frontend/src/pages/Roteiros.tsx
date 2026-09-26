import { useEffect, useState } from "react";
import { api } from "../api/client";
import type { Motorista, Ponto, Roteiro } from "../types/domain";
import Pill from "../components/Pill";

interface PontoRascunho {
  ordem: number;
  pontoId: string;
}

const rascunhoInicial: PontoRascunho[] = [
  { ordem: 1, pontoId: "" }, { ordem: 2, pontoId: "" },
  { ordem: 3, pontoId: "" }, { ordem: 4, pontoId: "" },
];

export default function Roteiros() {
  const [pontos, setPontos] = useState<PontoRascunho[]>(rascunhoInicial);
  const [catalogo, setCatalogo] = useState<Ponto[]>([]);
  const [motoristas, setMotoristas] = useState<Motorista[]>([]);
  const [roteiros, setRoteiros] = useState<Roteiro[]>([]);
  const [data, setData] = useState(new Date().toISOString().slice(0, 10));
  const [motoristaId, setMotoristaId] = useState("");
  const [distancia, setDistancia] = useState("1");
  const [erro, setErro] = useState("");

  useEffect(() => {
    Promise.all([api.listarMotoristas(), api.listarPontos(), api.listarRoteiros(data, data)])
      .then(([ms, ps, rs]) => { setMotoristas(ms); setCatalogo(ps); setRoteiros(rs); setMotoristaId(ms[0]?.id ?? ""); })
      .catch((e) => setErro(e.message));
  }, [data]);

  function adicionarPonto() {
    setPontos((atual) => [
      ...atual,
      { ordem: atual.length + 1, pontoId: "" },
    ]);
  }

  function atualizarPonto(ordem: number, valor: string) {
    setPontos((atual) =>
      atual.map((p) => (p.ordem === ordem ? { ...p, pontoId: valor } : p))
    );
  }

  async function salvar() {
    try {
      const criado = await api.criarRoteiro({ data, motoristaId, distanciaTotalKm: Number(distancia), pontos: pontos.map(({ pontoId, ordem }) => ({ pontoId, ordem })) });
      setRoteiros((atual) => [criado, ...atual]);
      alert("Roteiro salvo com sucesso.");
    } catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível salvar o roteiro"); }
  }

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Montar roteiro diário</h1>
          <div className="meta">
            Um roteiro pertence a um único motorista e a uma única data (RN05)
          </div>
        </div>
      </div>
      {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}

      <div className="form-split">
        <div className="form-card" style={{ maxWidth: "none" }}>
          <h3>Novo roteiro</h3>
          <p className="hint">
            Associe pontos em ordem sequencial — a ordem define o trajeto do
            dia (RN06).
          </p>

          <div className="field-row">
            <div className="field">
              <label>Data do roteiro</label>
              <input
                type="date"
                value={data}
                onChange={(e) => setData(e.target.value)}
              />
            </div>
            <div className="field">
              <label>Distância total (km)</label>
              <input type="number" min="0.1" step="0.1" value={distancia} onChange={(e) => setDistancia(e.target.value)} />
            </div>
            <div className="field">
              <label>Motorista / motoboy</label>
              <select
                value={motoristaId}
                onChange={(e) => setMotoristaId(e.target.value)}
              >
                {motoristas.map((m) => (
                  <option key={m.id} value={m.id}>
                    {m.nome} — {m.veiculo}
                  </option>
                ))}
              </select>
            </div>
          </div>

          <label
            style={{
              display: "block",
              fontSize: 12.5,
              fontWeight: 600,
              margin: "16px 0 8px",
            }}
          >
            Pontos do roteiro, em ordem
          </label>
          <div className="stoplist">
            {pontos.map((p) => (
              <div className="stoprow" key={p.ordem}>
                <span className="grip">⠿</span>
                <span className="n">{p.ordem}</span>
                <select className="addr" value={p.pontoId} onChange={(e) => atualizarPonto(p.ordem, e.target.value)}>
                  <option value="">Selecione o ponto</option>
                  {catalogo.map((ponto) => <option key={ponto.id} value={ponto.id}>{ponto.endereco}</option>)}
                </select>
              </div>
            ))}
          </div>
          <button className="btn" style={{ marginBottom: 6 }} onClick={adicionarPonto}>
            + Adicionar ponto ao roteiro
          </button>

          <div className="form-actions">
            <button className="btn primary" onClick={salvar}>
              Salvar roteiro
            </button>
            <button
              className="btn"
              onClick={() => setPontos(rascunhoInicial)}
            >
              Cancelar
            </button>
          </div>
        </div>

        <div className="panel">
          <h3>Roteiros de hoje</h3>
          <table>
            <thead>
              <tr>
                <th>Motorista</th>
                <th className="num">Pontos</th>
                <th>Status</th>
              </tr>
            </thead>
            <tbody>
              {roteiros.map((r) => (
                <tr key={r.id}>
                  <td>{motoristas.find((m) => m.id === r.motoristaId)?.nome ?? "—"}</td>
                  <td className="num">{r.pontos.length || 4}</td>
                  <td>
                    <Pill tone={r.status === "concluido" ? "ok" : "warn"}>
                      {r.status === "concluido" ? "Concluído" : "Em andamento"}
                    </Pill>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      </div>
    </section>
  );
}
