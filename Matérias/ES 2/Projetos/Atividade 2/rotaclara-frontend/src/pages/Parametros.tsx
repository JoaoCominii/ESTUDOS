import { useState } from "react";
import { parametro as parametroInicial } from "../data/mock";
import { calcularCustoPorKm } from "../domain/regras";

export default function Parametros() {
  const [valorCombustivel, setValorCombustivel] = useState(
    String(parametroInicial.valorCombustivelPorLitro).replace(".", ",")
  );
  const [rendimentoMedio, setRendimentoMedio] = useState("15");
  const [jornada, setJornada] = useState(String(parametroInicial.jornadaPadraoHoras));
  const [limiteAlerta, setLimiteAlerta] = useState(
    String(parametroInicial.limiteAlertaParadaMinutos)
  );
  const [salvo, setSalvo] = useState(false);

  const custoPorKmSugerido = calcularCustoPorKm(
    Number(valorCombustivel.replace(",", ".")) || 0,
    Number(rendimentoMedio) || 1
  );

  function salvar() {
    // Integração real: PUT /parametros no back-end.
    setSalvo(true);
    setTimeout(() => setSalvo(false), 2500);
  }

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Parâmetros de custo e jornada</h1>
          <div className="meta">
            Alteráveis sem mudança de código — critério de aceitação do MVP
          </div>
        </div>
      </div>

      <div className="form-card" style={{ maxWidth: 640 }}>
        <h3>Parâmetros gerais</h3>
        <p className="hint">
          Usados no cálculo automático de custo do roteiro (RN07) e nos
          indicadores de tempo parado (RN04).
        </p>

        <div className="field-row">
          <div className="field">
            <label>Valor do combustível (R$/litro)</label>
            <input
              type="text"
              className="mono"
              value={valorCombustivel}
              onChange={(e) => setValorCombustivel(e.target.value)}
            />
          </div>
          <div className="field">
            <label>Rendimento médio da frota (km/l)</label>
            <input
              type="text"
              className="mono"
              value={rendimentoMedio}
              onChange={(e) => setRendimentoMedio(e.target.value)}
            />
            <span className="fh">
              Custo por km sugerido: R${" "}
              {custoPorKmSugerido.toFixed(2).replace(".", ",")}
            </span>
          </div>
        </div>

        <div className="field-row">
          <div className="field">
            <label>Jornada padrão (horas/dia)</label>
            <input
              type="text"
              className="mono"
              value={jornada}
              onChange={(e) => setJornada(e.target.value)}
            />
            <span className="fh">
              Base percentual dos indicadores de tempo parado (RN04)
            </span>
          </div>
          <div className="field">
            <label>Limite de parada sem alerta (min)</label>
            <input
              type="text"
              className="mono"
              value={limiteAlerta}
              onChange={(e) => setLimiteAlerta(e.target.value)}
            />
            <span className="fh">
              Acima disso, o ponto é sinalizado no painel
            </span>
          </div>
        </div>

        <div className="form-actions">
          <button className="btn primary" onClick={salvar}>
            Salvar parâmetros
          </button>
          <button
            className="btn"
            onClick={() => {
              setValorCombustivel(
                String(parametroInicial.valorCombustivelPorLitro).replace(".", ",")
              );
              setJornada(String(parametroInicial.jornadaPadraoHoras));
              setLimiteAlerta(String(parametroInicial.limiteAlertaParadaMinutos));
            }}
          >
            Restaurar padrão
          </button>
          {salvo && (
            <span style={{ color: "var(--teal)", fontSize: 13, alignSelf: "center" }}>
              ✓ Parâmetros salvos
            </span>
          )}
        </div>
      </div>
    </section>
  );
}
