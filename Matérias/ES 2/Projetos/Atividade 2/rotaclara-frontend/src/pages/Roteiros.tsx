import { useState } from "react";
import { motoristas, nomeMotorista, roteiros } from "../data/mock";
import Pill from "../components/Pill";

interface PontoRascunho {
  ordem: number;
  endereco: string;
  eta: string;
}

const rascunhoInicial: PontoRascunho[] = [
  { ordem: 1, endereco: "Seg. Família — ponto de partida", eta: "08:00" },
  { ordem: 2, endereco: "Rua Peru, 55", eta: "08:20" },
  { ordem: 3, endereco: "Rua X, 5", eta: "08:50" },
  { ordem: 4, endereco: "Av. João César, 340", eta: "09:15" },
];

export default function Roteiros() {
  const [pontos, setPontos] = useState<PontoRascunho[]>(rascunhoInicial);
  const [data, setData] = useState("2026-09-23");
  const [motoristaId, setMotoristaId] = useState(motoristas[0]?.id ?? "");

  function adicionarPonto() {
    setPontos((atual) => [
      ...atual,
      { ordem: atual.length + 1, endereco: "", eta: "" },
    ]);
  }

  function atualizarPonto(ordem: number, campo: "endereco" | "eta", valor: string) {
    setPontos((atual) =>
      atual.map((p) => (p.ordem === ordem ? { ...p, [campo]: valor } : p))
    );
  }

  function salvar() {
    // Integração real: POST /roteiros { data, motoristaId, pontos } no back-end.
    alert(
      `Roteiro de ${data} para ${nomeMotorista(
        motoristaId
      )} salvo com ${pontos.length} pontos (simulado — sem back-end integrado ainda).`
    );
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
                <input
                  className="addr"
                  style={{
                    border: "none",
                    background: "none",
                    fontSize: 13,
                    padding: 0,
                  }}
                  value={p.endereco}
                  placeholder="Endereço do ponto"
                  onChange={(e) =>
                    atualizarPonto(p.ordem, "endereco", e.target.value)
                  }
                />
                <input
                  className="eta mono"
                  style={{
                    border: "none",
                    background: "none",
                    width: 56,
                    padding: 0,
                    textAlign: "right",
                  }}
                  value={p.eta}
                  placeholder="hh:mm"
                  onChange={(e) => atualizarPonto(p.ordem, "eta", e.target.value)}
                />
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
                  <td>{nomeMotorista(r.motoristaId)}</td>
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
