// ============================================================================
// geometria.js — Operações espaciais puras do FarmáciaFinder
// ----------------------------------------------------------------------------
// Componente responsável pelas consultas espaciais geográficas:
//   - Distância entre pontos (Haversine)
//   - Ponto dentro de polígono (ray casting)
//   - Distância ponto-polígono / ponto-círculo
//   - Avaliação dos operadores espaciais:
//       'estacontido' (ponto estritamente dentro da geometria)
//       'contem'      (a geometria contém o estabelecimento E sua área de
//                      influência/buffer está totalmente contida na geometria)
//       'intercepta'  (o buffer do estabelecimento toca ou cruza a geometria)
// ----------------------------------------------------------------------------
// Modelo de dados de geometria (ConsultaEspacial.geometria):
//   { tipo: 'poligono', vertices: [[lat, lng], ...] }
//   { tipo: 'circulo',  centro: { lat, lng }, raio: number (metros) }
// ============================================================================

export const OPERADORES_ESPACIAIS = {
  estacontido: {
    id: 'estacontido',
    rotulo: 'Está contido (Within)',
    descricao: 'O ponto do estabelecimento está estritamente dentro da área.'
  },
  contem: {
    id: 'contem',
    rotulo: 'Contém (Contains)',
    descricao: 'A área contém o estabelecimento e toda a sua área de influência (buffer).'
  },
  intercepta: {
    id: 'intercepta',
    rotulo: 'Intercepta (Intersects)',
    descricao: 'O estabelecimento ou seu buffer toca/intercepta a área.'
  }
};

const RAIO_TERRA_M = 6371000;
const EPS_METROS = 0.5;

// ---------------------------------------------------------------------------
// Haversine — distância em metros entre dois pontos geográficos
// ---------------------------------------------------------------------------
export function calcularDistancia(lat1, lng1, lat2, lng2) {
  const dLat = (lat2 - lat1) * Math.PI / 180;
  const dLng = (lng2 - lng1) * Math.PI / 180;
  const a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
            Math.sin(dLng / 2) * Math.sin(dLng / 2);
  const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
  return RAIO_TERRA_M * c;
}

// ---------------------------------------------------------------------------
// Ponto em segmento (tolerância em "graus" planares, suficiente para escala
// municipal). Usado para detectar ponto sobre a borda do polígono.
// ---------------------------------------------------------------------------
function pontoSobreSegmento(p, a, b, tol = 1e-9) {
  const cruz = (p[0] - a[0]) * (b[1] - a[1]) - (p[1] - a[1]) * (b[0] - a[0]);
  if (Math.abs(cruz) > tol) return false;
  const produto = (p[0] - a[0]) * (b[0] - a[0]) + (p[1] - a[1]) * (b[1] - a[1]);
  if (produto < 0) return false;
  const norma2 = (b[0] - a[0]) ** 2 + (b[1] - a[1]) ** 2;
  if (produto > norma2) return false;
  return true;
}

// ---------------------------------------------------------------------------
// Ray casting — o ponto está dentro do anel do polígono (incluindo a borda)?
// ---------------------------------------------------------------------------
export function pontoEmPoligono(lat, lng, anel) {
  if (!anel || anel.length < 3) return false;
  const p = [lng, lat];
  let dentro = false;

  for (let i = 0, j = anel.length - 1; i < anel.length; j = i++) {
    const ai = [anel[i][1], anel[i][0]];
    const aj = [anel[j][1], anel[j][0]];
    if (pontoSobreSegmento(p, ai, aj)) return true;
    const intersecta = ((ai[1] > lat) !== (aj[1] > lat)) &&
      (lng < (aj[0] - ai[0]) * (lat - ai[1]) / (aj[1] - ai[1]) + ai[0]);
    if (intersecta) dentro = !dentro;
  }
  return dentro;
}

// ---------------------------------------------------------------------------
// O ponto está estritamente dentro (sem contar a borda)?
// ---------------------------------------------------------------------------
export function pontoDentroPoligono(lat, lng, anel) {
  return pontoEmPoligono(lat, lng, anel) && !pontoSobreBorda(lat, lng, anel);
}

function pontoSobreBorda(lat, lng, anel) {
  const p = [lng, lat];
  for (let i = 0, j = anel.length - 1; i < anel.length; j = i++) {
    if (pontoSobreSegmento(p, [anel[i][1], anel[i][0]], [anel[j][1], anel[j][0]])) return true;
  }
  return false;
}

// ---------------------------------------------------------------------------
// Distância (em metros) de um ponto a um segmento [a, b] (lat/lng).
// ---------------------------------------------------------------------------
function distanciaPontoSegmento(p, a, b) {
  const ax = a[0], ay = a[1];
  const bx = b[0], by = b[1];
  const dx = bx - ax, dy = by - ay;
  const t = Math.max(0, Math.min(1, ((p[0] - ax) * dx + (p[1] - ay) * dy) / (dx * dx + dy * dy)));
  const px = ax + t * dx, py = ay + t * dy;
  return calcularDistancia(p[1], p[0], py, px);
}

// ---------------------------------------------------------------------------
// Distância mínima do ponto ao anel do polígono (0 se dentro). Também calcula
// a distância de um ponto INTERIOR até a borda mais próxima, útil para o
// operador 'contem'.
// ---------------------------------------------------------------------------
export function distanciaMinimaAoPoligono(lat, lng, anel) {
  const p = [lng, lat];
  let minima = Infinity;
  for (let i = 0, j = anel.length - 1; i < anel.length; j = i++) {
    const a = [anel[i][1], anel[i][0]];
    const b = [anel[j][1], anel[j][0]];
    minima = Math.min(minima, distanciaPontoSegmento(p, a, b));
  }
  return minima;
}

// ---------------------------------------------------------------------------
// Círculo
// ---------------------------------------------------------------------------
export function pontoDentroCirculo(lat, lng, centro, raio) {
  return calcularDistancia(lat, lng, centro.lat, centro.lng) <= raio;
}

// Distância do ponto até o círculo (0 se dentro do círculo).
export function distanciaPontoCirculo(lat, lng, centro, raio) {
  const d = calcularDistancia(lat, lng, centro.lat, centro.lng);
  return Math.max(0, d - raio);
}

// ---------------------------------------------------------------------------
// Distância do ponto até a geometria de consulta (0 se dentro).
// ---------------------------------------------------------------------------
export function distanciaPontoGeometria(ponto, geometria) {
  if (geometria.tipo === 'poligono') {
    return pontoDentroPoligono(ponto.lat, ponto.lng, geometria.vertices)
      ? 0
      : distanciaMinimaAoPoligono(ponto.lat, ponto.lng, geometria.vertices);
  }
  if (geometria.tipo === 'circulo') {
    return distanciaPontoCirculo(ponto.lat, ponto.lng, geometria.centro, geometria.raio);
  }
  throw new Error(`Geometria desconhecida: ${geometria.tipo}`);
}

// ---------------------------------------------------------------------------
// Avalia se um estabelecimento (ponto) atende a um operador espacial.
//   operador: 'estacontido' | 'contem' | 'intercepta'
//   raioInfluencia: buffer em metros em torno do estabelecimento usado por
//                   'contem' e 'intercepta' (default 0).
// ---------------------------------------------------------------------------
export function avaliarOperadorEspacial(ponto, geometria, operador, raioInfluencia = 0) {
  if (!geometria) return true;
  const buffer = Math.max(0, Number(raioInfluencia) || 0);

  switch (operador) {
    case 'estacontido':
      if (geometria.tipo === 'poligono') return pontoDentroPoligono(ponto.lat, ponto.lng, geometria.vertices);
      if (geometria.tipo === 'circulo') return pontoDentroCirculo(ponto.lat, ponto.lng, geometria.centro, geometria.raio);
      break;

    case 'contem':
      if (geometria.tipo === 'poligono') {
        const dentro = pontoDentroPoligono(ponto.lat, ponto.lng, geometria.vertices);
        if (!dentro) return false;
        return distanciaMinimaAoPoligono(ponto.lat, ponto.lng, geometria.vertices) >= buffer - EPS_METROS;
      }
      if (geometria.tipo === 'circulo') {
        const d = calcularDistancia(ponto.lat, ponto.lng, geometria.centro.lat, geometria.centro.lng);
        return d + buffer <= geometria.raio + EPS_METROS;
      }
      break;

    case 'intercepta':
      if (geometria.tipo === 'poligono') {
        return pontoEmPoligono(ponto.lat, ponto.lng, geometria.vertices) ||
               distanciaMinimaAoPoligono(ponto.lat, ponto.lng, geometria.vertices) <= buffer + EPS_METROS;
      }
      if (geometria.tipo === 'circulo') {
        const d = calcularDistancia(ponto.lat, ponto.lng, geometria.centro.lat, geometria.centro.lng);
        return d <= geometria.raio + buffer + EPS_METROS;
      }
      break;

    default:
      throw new Error(`Operador desconhecido: ${operador}`);
  }
  return false;
}

// ---------------------------------------------------------------------------
// Bounds (canto sudoeste / nordeste) de uma área circular, usado para
// enquadrar o mapa na visualização.
// ---------------------------------------------------------------------------
export function calcularBoundsEmVolta(centro, raio) {
  const latDelta = raio / 111320;
  const lngDelta = raio / (111320 * Math.max(Math.cos(centro.lat * Math.PI / 180), 0.01));
  return {
    sul: centro.lat - latDelta,
    oeste: centro.lng - lngDelta,
    norte: centro.lat + latDelta,
    leste: centro.lng + lngDelta
  };
}
