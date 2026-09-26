import { useEffect, useState } from "react";
import { api } from "../api/client";

interface PontoCadastro {
  id: string;
  endereco: string;
  latitude: string;
  longitude: string;
}

export default function Pontos() {
  const [pontos, setPontos] = useState<PontoCadastro[]>([]);
  const [endereco, setEndereco] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");
  const [erro, setErro] = useState("");

  useEffect(() => {
    api.listarPontos().then((items) => setPontos(items)).catch((e) => setErro(e.message));
  }, []);

  async function salvar() {
    if (!endereco.trim()) return;
    try {
      const criado = await api.criarPonto({ endereco, latitude: Number(latitude), longitude: Number(longitude) });
      setPontos((atual) => [...atual, criado]);
      setEndereco(""); setLatitude(""); setLongitude("");
    } catch (e) { setErro(e instanceof Error ? e.message : "Não foi possível salvar"); }
  }

  return (
    <section>
      <div className="topline">
        <div>
          <h1>Pontos</h1>
          <div className="meta">
            Endereço e coordenadas usados na montagem dos roteiros
          </div>
        </div>
      </div>
      {erro && <p style={{ color: "var(--rust)" }}>{erro}</p>}

      <div className="form-split">
        <div className="form-card" style={{ maxWidth: "none" }}>
          <h3>Cadastrar ponto</h3>
          <div className="field">
            <label>Endereço</label>
            <input
              type="text"
              placeholder="Ex.: Rua Peru, 55 — Sion, Belo Horizonte"
              value={endereco}
              onChange={(e) => setEndereco(e.target.value)}
            />
          </div>
          <div className="field-row">
            <div className="field">
              <label>Latitude</label>
              <input
                type="text"
                className="mono"
                placeholder="-19.9420"
                value={latitude}
                onChange={(e) => setLatitude(e.target.value)}
              />
            </div>
            <div className="field">
              <label>Longitude</label>
              <input
                type="text"
                className="mono"
                placeholder="-43.9378"
                value={longitude}
                onChange={(e) => setLongitude(e.target.value)}
              />
            </div>
          </div>
          <div className="form-actions">
            <button className="btn primary" onClick={salvar}>
              Salvar ponto
            </button>
            <button
              className="btn"
              onClick={() => {
                setEndereco("");
                setLatitude("");
                setLongitude("");
              }}
            >
              Cancelar
            </button>
          </div>
        </div>

        <div className="table-card" style={{ alignSelf: "start" }}>
          <div className="table-card-head">
            <h3>Pontos cadastrados</h3>
          </div>
          <table>
            <thead>
              <tr>
                <th>Endereço</th>
                <th className="num">Lat / Long</th>
              </tr>
            </thead>
            <tbody>
              {pontos.map((p) => (
                <tr key={p.id}>
                  <td>{p.endereco}</td>
                  <td className="num">
                    {p.latitude} / {p.longitude}
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
