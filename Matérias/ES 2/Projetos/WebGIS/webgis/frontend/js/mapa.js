// ============================================================================
// mapa.js — Componente "Mapa"
// ----------------------------------------------------------------------------
// Responsável por:
//   - Exibir o mapa (Leaflet + OpenStreetMap)
//   - Marcar o centro de referência e desenhar o raio de busca
//   - Renderizar resultados (farmácias e laboratórios) no mapa
//   - Desenhar polígonos para consultas espaciais
//   - Escolher um novo centro por clique (mudança de centro)
//   - Compartilhar a visão do mapa (move/zoom) em tempo real
// ============================================================================

const IDS = { mapa: 'mapa' };

let mapa = null;
let camadaCentro = null;
let camadaRaio = null;
let camadaResultados = null;
let camadaPoligono = null;
let modoEscolherCentro = false;

const callbacks = {
  aoMover: () => {},
  aoEscolherCentro: () => {},
  aoPoligonoDesenhado: () => {}
};

export function criarMapa({ onMover, onEscolherCentro, onPoligonoDesenhado }) {
  callbacks.aoMover = onMover;
  callbacks.aoEscolherCentro = onEscolherCentro;
  callbacks.aoPoligonoDesenhado = onPoligonoDesenhado;

  mapa = L.map(IDS.mapa).setView([-19.9205, -43.9377], 13);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; OpenStreetMap contributors',
    maxZoom: 19
  }).addTo(mapa);

  camadaCentro = L.layerGroup().addTo(mapa);
  camadaRaio = L.layerGroup().addTo(mapa);
  camadaResultados = L.layerGroup().addTo(mapa);

  mapa.on('moveend', () => callbacks.aoMover());
  mapa.on('click', (e) => {
    if (modoEscolherCentro) {
      callbacks.aoEscolherCentro(e.latlng.lat, e.latlng.lng);
    }
  });

  return mapa;
}

export function obterMapa() {
  return mapa;
}

// ---------------------------------------------------------------------------
// Centro de referência + raio de busca
// ---------------------------------------------------------------------------
export function renderizarCentro(centro, raio) {
  camadaCentro.clearLayers();

  if (centro) {
    const icone = L.divIcon({
      className: 'marcador-centro',
      html: '<span>🏥</span>',
      iconSize: [36, 36]
    });
    const marcador = L.marker([centro.lat, centro.lng], { icon: icone, zIndexOffset: 1000 })
      .addTo(camadaCentro)
      .bindPopup(`<strong>${centro.nome}</strong><br><small>Centro de referência</small>`);
    marcador.on('click', () => marcador.openPopup());

    if (raio > 0) {
      L.circle([centro.lat, centro.lng], {
        radius: raio,
        color: '#2563eb',
        fillColor: '#2563eb',
        fillOpacity: 0.08,
        weight: 2,
        dashArray: '4 4'
      }).addTo(camadaRaio);
    }
  } else {
    camadaRaio.clearLayers();
  }
}

// ---------------------------------------------------------------------------
// Resultados (farmácias e laboratórios)
// ---------------------------------------------------------------------------
export function renderizarResultados(resultados) {
  camadaResultados.clearLayers();

  resultados.forEach(({ estabelecimento, distancia }) => {
    const icone = L.divIcon({
      className: `marcador-resultado tipo-${estabelecimento.tipo}`,
      html: `<span>${estabelecimento.tipo === 'farmacia' ? '💊' : '🔬'}</span>`,
      iconSize: [28, 28]
    });

    const marcador = L.marker([estabelecimento.lat, estabelecimento.lng], { icon: icone })
      .addTo(camadaResultados)
      .bindPopup(`
        <strong>${estabelecimento.nome}</strong><br>
        <small>${estabelecimento.tipo === 'farmacia' ? 'Farmácia' : 'Laboratório'}</small><br>
        <small>📍 ${estabelecimento.endereco || 'Endereço não disponível'}</small><br>
        <small>📏 ${Math.round(distancia)} m do centro</small>
      `);
    marcador.on('click', () => marcador.openPopup());
  });
}

export function limparResultados() {
  camadaResultados.clearLayers();
}

export function voarPara(estabelecimento) {
  mapa.setView([estabelecimento.lat, estabelecimento.lng], 16);
}

export function enquadrar(centro, raio) {
  if (!centro) return;
  const bounds = raio
    ? [[centro.lat - raio / 111320, centro.lng - raio / (111320 * Math.cos(centro.lat * Math.PI / 180))],
       [centro.lat + raio / 111320, centro.lng + raio / (111320 * Math.cos(centro.lat * Math.PI / 180))]]
    : [[centro.lat - 0.01, centro.lng - 0.01], [centro.lat + 0.01, centro.lng + 0.01]];
  mapa.fitBounds(bounds, { padding: [30, 30] });
}

export function definirVisao(centro, zoom) {
  if (centro && !Number.isNaN(centro.lat) && !Number.isNaN(centro.lng)) {
    mapa.setView([centro.lat, centro.lng], zoom || mapa.getZoom());
  }
}

export function obterVisao() {
  const c = mapa.getCenter();
  return { lat: c.lat, lng: c.lng, zoom: mapa.getZoom() };
}

// ---------------------------------------------------------------------------
// Desenho de polígono (consulta espacial)
// ---------------------------------------------------------------------------
let drawnItems = null;

export function habilitarDesenhoPoligono() {
  if (!drawnItems) {
    drawnItems = new L.FeatureGroup();
    mapa.addLayer(drawnItems);
    const controle = new L.Control.Draw({
      edit: { featureGroup: drawnItems, remove: false },
      draw: {
        polygon: { allowIntersection: false, showArea: true },
        polyline: false, rectangle: false, circle: false,
        marker: false, circlemarker: false
      }
    });
    mapa.addControl(controle);

    mapa.on(L.Draw.Event.CREATED, (evento) => {
      if (camadaPoligono) {
        mapa.removeLayer(camadaPoligono);
        drawnItems.clearLayers();
      }
      camadaPoligono = evento.layer;
      drawnItems.addLayer(evento.layer);
      const geoJSON = evento.layer.toGeoJSON();
      const vertices = geoJSON.geometry.coordinates[0].map(([lng, lat]) => [lat, lng]);
      callbacks.aoPoligonoDesenhado({ tipo: 'poligono', vertices });
    });
  }
  // Abre o desenho imediatamente
  new L.Draw.Polygon(mapa, { allowIntersection: false, showArea: true }).enable();
}

export function limparPoligono() {
  if (camadaPoligono) {
    mapa.removeLayer(camadaPoligono);
    camadaPoligono = null;
  }
  if (drawnItems) drawnItems.clearLayers();
}

// ---------------------------------------------------------------------------
// Escolha de centro pelo clique no mapa (mudança de centro)
// ---------------------------------------------------------------------------
export function ativarEscolhaDeCentro(ativo) {
  modoEscolherCentro = ativo;
}
