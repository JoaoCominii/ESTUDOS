import { useState } from "react";
import { motoristas as motoristasIniciais } from "../data/mock";
import type { Motorista } from "../types/domain";

export default function Motoristas() {
  const [motoristas, setMotoristas] = useState<Motorista[]>(motoristasIniciais);
  const [nome, setNome] = useState("");
  const [telefone, setTelefone] = useState("");
  const [documento, setDocumento] = useState("");
  const [veiculo, setVeiculo] = useState("");
  const [rendimento, setRendimento] = useState("");

  function limpar() {
    setNome("");
    setTelefone("");
    setDocumento("");
    setVeiculo("");
    setRendimento("");
  }

  function salvar() {
    if (!nome.trim()) return;
    setMotoristas((atual) => [
      ...atual,
      {
        id: crypto.randomUUID(),
        nome,
        telefone,
        documento,
        veiculo,
        rendimentoKmLitro: Number(rendimento) || 0,
      },
    ]);
    limpar();
  }

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Motoristas e motoboys</h1>
          <div className="meta">
            Dados usados no roteiro e no cálculo de custo (RN07)
          </div>
        </div>
      </div>

      <div className="form-split">
        <div className="form-card" style={{ maxWidth: "none" }}>
          <h3>Cadastrar motorista/motoboy</h3>
          <div className="field">
            <label>Nome completo</label>
            <input
              type="text"
              placeholder="Ex.: Carlos Mendes"
              value={nome}
              onChange={(e) => setNome(e.target.value)}
            />
          </div>
          <div className="field-row">
            <div className="field">
              <label>Telefone</label>
              <input
                type="text"
                placeholder="(31) 9 9999-0000"
                value={telefone}
                onChange={(e) => setTelefone(e.target.value)}
              />
            </div>
            <div className="field">
              <label>Documento</label>
              <input
                type="text"
                placeholder="CPF"
                value={documento}
                onChange={(e) => setDocumento(e.target.value)}
              />
            </div>
          </div>
          <div className="field-row">
            <div className="field">
              <label>Veículo</label>
              <input
                type="text"
                placeholder="Ex.: Moto Honda CG 160"
                value={veiculo}
                onChange={(e) => setVeiculo(e.target.value)}
              />
            </div>
            <div className="field">
              <label>Rendimento (km/litro)</label>
              <input
                type="text"
                className="mono"
                placeholder="35"
                value={rendimento}
                onChange={(e) => setRendimento(e.target.value)}
              />
            </div>
          </div>
          <div className="form-actions">
            <button className="btn primary" onClick={salvar}>
              Salvar motorista
            </button>
            <button className="btn" onClick={limpar}>
              Cancelar
            </button>
          </div>
        </div>

        <div className="table-card" style={{ alignSelf: "start" }}>
          <div className="table-card-head">
            <h3>Equipe</h3>
          </div>
          <table>
            <thead>
              <tr>
                <th>Nome</th>
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
    </section>
  );
}
