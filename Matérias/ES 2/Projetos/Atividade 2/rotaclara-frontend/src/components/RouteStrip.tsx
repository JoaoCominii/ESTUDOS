import type { Parametro, Roteiro } from "../types/domain";
import {
  calcularTempoParadoPonto,
  pontoEmAlerta,
} from "../domain/regras";
import { horaFormatada, tempoParadoFormatado } from "../data/mock";

/**
 * Representa visualmente os pontos de um roteiro em sequência.
 * A largura de cada parada é proporcional ao tempo parado
 * (RN01/RN02) — o ponto de partida recebe uma largura mínima fixa,
 * já que nunca conta tempo parado.
 */
export default function RouteStrip({
  roteiro,
  parametro,
}: {
  roteiro: Roteiro;
  parametro: Parametro;
}) {
  return (
    <div className="route-strip">
      {roteiro.pontos.map((ponto) => {
        if (ponto.ordemNoRoteiro === 1) {
          return (
            <div key={ponto.id} className="stop partida" style={{ flex: "6 1 0" }}>
              <span className="t">{horaFormatada(ponto.dataHoraChegada)}</span>
              <span className="a">{ponto.endereco}</span>
            </div>
          );
        }

        const minutos = calcularTempoParadoPonto(ponto);
        const alerta = pontoEmAlerta(ponto, parametro);
        const flexGrow = minutos && minutos > 0 ? minutos : 8;

        return (
          <div
            key={ponto.id}
            className={`stop parada ${alerta ? "alerta" : ""}`}
            style={{ flex: `${flexGrow} 1 0` }}
            title={`${ponto.endereco} — ${tempoParadoFormatado(minutos)}`}
          >
            <span className="t">{tempoParadoFormatado(minutos)}</span>
            <span className="a">{ponto.endereco}</span>
          </div>
        );
      })}
    </div>
  );
}
