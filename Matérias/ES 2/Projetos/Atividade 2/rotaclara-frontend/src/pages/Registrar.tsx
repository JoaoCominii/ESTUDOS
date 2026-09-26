import { useEffect, useRef, useState } from "react";
import { api } from "../api/client";
import type { Parametro, Roteiro } from "../types/domain";

type Fase = "aguardando_chegada" | "parado" | "roteiro_concluido";

function formatarCronometro(segundos: number): string {
  const h = String(Math.floor(segundos / 3600)).padStart(2, "0");
  const m = String(Math.floor((segundos % 3600) / 60)).padStart(2, "0");
  const s = String(segundos % 60).padStart(2, "0");
  return `${h}:${m}:${s}`;
}

export default function Registrar() {
  const [roteiro, setRoteiro] = useState<Roteiro | null>(null);
  const [parametro, setParametro] = useState<Parametro>({ valorCombustivelPorLitro: 0, custoPorKm: 0, jornadaPadraoHoras: 8, limiteAlertaParadaMinutos: 30 });
  const [indice, setIndice] = useState(0);
  const [fase, setFase] = useState<Fase>("aguardando_chegada");
  const [segundos, setSegundos] = useState(0);
  const [erro, setErro] = useState("");
  const intervalo = useRef<number | null>(null);

  useEffect(() => {
    const hoje = new Date().toISOString().slice(0, 10);
    Promise.all([api.listarRoteiros(hoje, hoje), api.buscarParametro()])
      .then(([roteiros, p]) => { setRoteiro(roteiros[0] ?? null); setParametro(p); })
      .catch((e) => setErro(e.message));
  }, []);

  const pontoAtual = roteiro?.pontos[indice];
  const alerta = segundos / 60 > parametro.limiteAlertaParadaMinutos;

  useEffect(() => {
    if (fase !== "parado") return;
    intervalo.current = window.setInterval(() => setSegundos((s) => s + 1), 1000);
    return () => { if (intervalo.current) window.clearInterval(intervalo.current); };
  }, [fase]);

  async function registrarChegada() {
    if (!roteiro || !pontoAtual) return;
    try { setRoteiro(await api.registrarChegada(roteiro.id, pontoAtual.id)); setSegundos(0); setFase("parado"); }
    catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível registrar chegada"); }
  }

  async function registrarSaida() {
    if (!roteiro || !pontoAtual) return;
    try {
      const atualizado = await api.registrarSaida(roteiro.id, pontoAtual.id);
      setRoteiro(atualizado);
      if (indice + 1 >= atualizado.pontos.length) setFase("roteiro_concluido");
      else { setIndice((i) => i + 1); setSegundos(0); setFase("aguardando_chegada"); }
    } catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível registrar saída"); }
  }

  return (
    <section>
      <div className="topline"><div><h1>Registrar chegada e saída</h1><div className="meta">Tela do motorista/motoboy — uso previsto em dispositivo móvel (RNF02)</div></div></div>
      {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}
      {!roteiro && !erro && <p>Carregando roteiro de hoje...</p>}
      {roteiro && pontoAtual && <div className="phone-wrap">
        <div className="phone"><div className="phone-screen">
          <div className="phone-status"><span>{new Date().toLocaleTimeString("pt-BR", { hour: "2-digit", minute: "2-digit" })}</span><span>●●● RotaClara</span></div>
          <div className="phone-head"><div className="greet">Roteiro de hoje</div><div className="name">Motorista {roteiro.motoristaId}</div></div>
          <div className="phone-body">
            {fase === "roteiro_concluido" ? <div className="stop-card" style={{ background: "var(--teal-tint)", borderColor: "#9FD2C0" }}><div className="lbl" style={{ color: "var(--teal)" }}>Roteiro concluído</div><div className="addr">Todos os {roteiro.pontos.length} pontos foram visitados.</div></div> : <>
              <div className={`stop-card ${alerta ? "alerta" : ""}`}><div className="lbl">Ponto {pontoAtual.ordemNoRoteiro} de {roteiro.pontos.length}</div><div className="addr">{pontoAtual.endereco}</div>
                {fase === "parado" && pontoAtual.ordemNoRoteiro !== 1 && <><div className="timer">{formatarCronometro(segundos)}</div><div className="timer-lbl">tempo parado neste ponto</div></>}
                {fase === "parado" && pontoAtual.ordemNoRoteiro === 1 && <div className="timer-lbl">ponto de partida — não conta tempo parado (RN01)</div>}
              </div>
              {fase === "aguardando_chegada" ? <button className="big-btn" onClick={registrarChegada}>Registrar chegada</button> : <button className="big-btn" onClick={registrarSaida}>{indice + 1 >= roteiro.pontos.length ? "Registrar saída (finalizar)" : "Registrar saída"}</button>}
              <div className="next-list"><div className="lbl">Próximos pontos</div>{roteiro.pontos.slice(indice + 1).map((p) => <div className="next-item" key={p.id}><span className="n">{p.ordemNoRoteiro}</span>{p.endereco}</div>)}{indice + 1 >= roteiro.pontos.length && <div className="next-item"><span className="n">—</span>nenhum ponto restante</div>}</div>
            </>}
          </div>
        </div></div>
        <div className="phone-note"><strong>Como funciona</strong>Ao chegar em um ponto, o motorista toca em "Registrar chegada"; o cronômetro começa e o tempo parado é calculado automaticamente na saída (RN02).<br /><br />O ponto de partida não exibe cronômetro de parada — apenas confirma o início do roteiro (RN01).<br /><br />Se o tempo parado passar de {parametro.limiteAlertaParadaMinutos} minutos, o card muda de âmbar para vermelho.</div>
      </div>}
    </section>
  );
}
