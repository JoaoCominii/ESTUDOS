import type { Parametro, Ponto, Roteiro } from "../types/domain";

/**
 * RN02 — O tempo parado em um ponto é a diferença entre o horário
 * de saída e o horário de chegada naquele ponto.
 *
 * RN01 — O ponto de partida (ordemNoRoteiro === 1) não conta tempo
 * parado: o cronômetro só é considerado a partir do segundo ponto.
 *
 * Retorna `null` enquanto o ponto ainda não tem chegada e saída
 * registradas (ponto em aberto).
 */
export function calcularTempoParadoPonto(ponto: Ponto): number | null {
  if (ponto.ordemNoRoteiro === 1) return 0; // RN01

  if (!ponto.dataHoraChegada || !ponto.dataHoraSaida) return null;

  const chegada = new Date(ponto.dataHoraChegada).getTime();
  const saida = new Date(ponto.dataHoraSaida).getTime();
  const minutos = Math.round((saida - chegada) / 60000);

  return Math.max(minutos, 0);
}

/**
 * RN03 — O tempo total parado do roteiro é a soma dos tempos parados
 * de todos os pontos, exceto o ponto de partida.
 *
 * Pontos ainda em aberto (sem saída registrada) não entram na soma.
 */
export function calcularTempoTotalParadoRoteiro(roteiro: Roteiro): number {
  return roteiro.pontos
    .filter((p) => p.ordemNoRoteiro !== 1)
    .reduce((total, p) => {
      const tempo = calcularTempoParadoPonto(p);
      return total + (tempo ?? 0);
    }, 0);
}

/**
 * RN07 — O custo do trajeto é calculado a partir do valor do
 * combustível, do rendimento km/litro do veículo e da distância
 * percorrida.
 *
 * Usamos o custo por km já parametrizado (`Parametro.custoPorKm`),
 * que por sua vez deve ter sido derivado de combustível ÷ rendimento
 * no cadastro de parâmetros — ver `calcularCustoPorKm` abaixo.
 */
export function calcularCustoRoteiro(
  roteiro: Roteiro,
  parametro: Parametro
): number {
  return Number((roteiro.distanciaTotalKm * parametro.custoPorKm).toFixed(2));
}

/**
 * Auxiliar de RN07: deriva o custo por km a partir do valor do
 * combustível e do rendimento (km/litro) de um veículo específico.
 * Útil na tela de Parâmetros para sugerir o custo por km a partir
 * do combustível e de um rendimento médio da frota.
 */
export function calcularCustoPorKm(
  valorCombustivelPorLitro: number,
  rendimentoKmLitro: number
): number {
  if (rendimentoKmLitro <= 0) return 0;
  return Number((valorCombustivelPorLitro / rendimentoKmLitro).toFixed(2));
}

/**
 * RN04 — a jornada padrão (horas/dia) serve de base percentual para
 * os indicadores de tempo parado. Retorna, por exemplo, 0.15 para
 * "15% da jornada foi tempo parado".
 */
export function percentualDaJornadaParado(
  tempoParadoMinutos: number,
  parametro: Parametro
): number {
  const jornadaMinutos = parametro.jornadaPadraoHoras * 60;
  if (jornadaMinutos <= 0) return 0;
  return tempoParadoMinutos / jornadaMinutos;
}

/** Indica se um ponto passou do limite de alerta parametrizado. */
export function pontoEmAlerta(
  ponto: Ponto,
  parametro: Parametro
): boolean {
  const tempo = calcularTempoParadoPonto(ponto);
  return tempo !== null && tempo > parametro.limiteAlertaParadaMinutos;
}
