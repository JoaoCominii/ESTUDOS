import { useEffect, useRef, useState } from "react";
import { parametro } from "../data/mock";

interface PontoRota {
  endereco: string;
  ordem: number;
  final?: boolean;
}

const pontosRota: PontoRota[] = [
  { endereco: "Seg. Família (partida)", ordem: 1 },
  { endereco: "Rua Peru, 55", ordem: 2 },
  { endereco: "Rua X, 5", ordem: 3 },
  { endereco: "Av. João César, 340", ordem: 4, final: true },
];

type Fase = "aguardando_chegada" | "parado" | "roteiro_concluido";

function formatarCronometro(segundos: number): string {
  const h = String(Math.floor(segundos / 3600)).padStart(2, "0");
  const m = String(Math.floor((segundos % 3600) / 60)).padStart(2, "0");
  const s = String(segundos % 60).padStart(2, "0");
  return `${h}:${m}:${s}`;
}

export default function Registrar() {
  const [indice, setIndice] = useState(0);
  // O primeiro ponto (partida) já "chega" automaticamente, sem cronômetro (RN01).
  const [fase, setFase] = useState<Fase>("parado");
  const [segundos, setSegundos] = useState(0);
  const intervalo = useRef<number | null>(null);

  const pontoAtual = pontosRota[indice];
  const alerta = segundos / 60 > parametro.limiteAlertaParadaMinutos;

  useEffect(() => {
    if (fase !== "parado") return;
    intervalo.current = window.setInterval(() => {
      setSegundos((s) => s + 1);
    }, 1000);
    return () => {
      if (intervalo.current) window.clearInterval(intervalo.current);
    };
  }, [fase]);

  function registrarChegada() {
    setSegundos(0);
    setFase("parado");
  }

  function registrarSaida() {
    // RN02: tempo parado = saída - chegada (aqui, o cronômetro em segundos).
    if (indice + 1 >= pontosRota.length) {
      setFase("roteiro_concluido");
      return;
    }
    setIndice((i) => i + 1);
    setFase("aguardando_chegada");
    setSegundos(0);
  }

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Registrar chegada e saída</h1>
          <div className="meta">
            Tela do motorista/motoboy — uso previsto em dispositivo móvel
            (RNF02)
          </div>
        </div>
      </div>

      <div className="phone-wrap">
        <div className="phone">
          <div className="phone-screen">
            <div className="phone-status">
              <span>{new Date().toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}</span>
              <span>●●● RotaClara</span>
            </div>
            <div className="phone-head">
              <div className="greet">Roteiro de hoje</div>
              <div className="name">Carlos Mendes — Roteiro A</div>
            </div>

            <div className="phone-body">
              {fase === "roteiro_concluido" ? (
                <div className="stop-card" style={{ background: "var(--teal-tint)", borderColor: "#9FD2C0" }}>
                  <div className="lbl" style={{ color: "var(--teal)" }}>
                    Roteiro concluído
                  </div>
                  <div className="addr">Todos os {pontosRota.length} pontos foram visitados.</div>
                </div>
              ) : (
                <>
                  <div className={`stop-card ${alerta ? "alerta" : ""}`}>
                    <div className="lbl">
                      Ponto {pontoAtual.ordem} de {pontosRota.length}
                    </div>
                    <div className="addr">{pontoAtual.endereco}</div>
                    {fase === "parado" && pontoAtual.ordem !== 1 && (
                      <>
                        <div className="timer">{formatarCronometro(segundos)}</div>
                        <div className="timer-lbl">tempo parado neste ponto</div>
                      </>
                    )}
                    {fase === "parado" && pontoAtual.ordem === 1 && (
                      <div className="timer-lbl">
                        ponto de partida — não conta tempo parado (RN01)
                      </div>
                    )}
                  </div>

                  {fase === "aguardando_chegada" ? (
                    <button className="big-btn" onClick={registrarChegada}>
                      Registrar chegada
                    </button>
                  ) : (
                    <button className="big-btn" onClick={registrarSaida}>
                      {pontoAtual.final ? "Registrar saída (finalizar)" : "Registrar saída"}
                    </button>
                  )}

                  <div className="next-list">
                    <div className="lbl">Próximos pontos</div>
                    {pontosRota.slice(indice + 1).map((p) => (
                      <div className="next-item" key={p.ordem}>
                        <span className="n">{p.ordem}</span>
                        {p.endereco}
                        {p.final ? " — ponto final" : ""}
                      </div>
                    ))}
                    {indice + 1 >= pontosRota.length && (
                      <div className="next-item">
                        <span className="n">—</span>
                        nenhum ponto restante
                      </div>
                    )}
                  </div>
                </>
              )}
            </div>
          </div>
        </div>

        <div className="phone-note">
          <strong>Como funciona</strong>
          Ao chegar em um ponto, o motorista toca em "Registrar chegada"; o
          cronômetro começa e o tempo parado é calculado automaticamente na
          saída (RN02).
          <br />
          <br />
          O ponto de partida não exibe cronômetro de parada — apenas
          confirma o início do roteiro (RN01).
          <br />
          <br />
          Se o tempo parado passar de {parametro.limiteAlertaParadaMinutos} minutos, o
          card muda de âmbar para vermelho, sinalizando um possível gargalo
          ao gerente no painel.
        </div>
      </div>
    </section>
  );
}
