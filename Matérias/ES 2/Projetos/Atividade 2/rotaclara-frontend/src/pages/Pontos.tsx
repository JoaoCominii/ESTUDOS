import { useState } from "react";

interface PontoCadastro {
  id: string;
  endereco: string;
  latitude: string;
  longitude: string;
}

const pontosIniciais: PontoCadastro[] = [
  { id: "1", endereco: "Seg. Família", latitude: "-19.9245", longitude: "-43.9352" },
  { id: "2", endereco: "Rua Peru, 55", latitude: "-19.9420", longitude: "-43.9378" },
  { id: "3", endereco: "Rua X, 5", latitude: "-19.9187", longitude: "-43.9401" },
  { id: "4", endereco: "Av. João César, 340", latitude: "-19.9033", longitude: "-43.9455" },
];

export default function Pontos() {
  const [pontos, setPontos] = useState<PontoCadastro[]>(pontosIniciais);
  const [endereco, setEndereco] = useState("");
  const [latitude, setLatitude] = useState("");
  const [longitude, setLongitude] = useState("");

  function salvar() {
    if (!endereco.trim()) return;
    setPontos((atual) => [
      ...atual,
      { id: crypto.randomUUID(), endereco, latitude, longitude },
    ]);
    setEndereco("");
    setLatitude("");
    setLongitude("");
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
