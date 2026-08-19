import { calcularBoundsEmVolta, calcularDistancia } from '../frontend/js/geometria.js';
import { teste, verdadeiro, proximo } from './harness.js';

// ---------------------------------------------------------------------------
// Testes do componente Mapa.
//
// A parte visual (Leaflet) exige navegador; portanto, os testes automatizados
// cobrem os cálculos geométricos usados pela visualização (enquadramento do
// mapa em torno do centro + raio). O checklist manual de visualização está em
// docs/TESTES.md.
// ---------------------------------------------------------------------------

console.log('\n[mapa] Enquadramento (fitBounds)');
// ---------------------------------------------------------------------------
const CENTRO = { lat: -19.9205, lng: -43.9377 };
const RAIO = 2000;

teste('bounds contêm o centro de referência', () => {
  const b = calcularBoundsEmVolta(CENTRO, RAIO);
  verdadeiro(CENTRO.lat >= b.sul && CENTRO.lat <= b.norte);
  verdadeiro(CENTRO.lng >= b.oeste && CENTRO.lng <= b.leste);
});

teste('todos os pontos dentro do raio cabem no bounds', () => {
  const b = calcularBoundsEmVolta(CENTRO, RAIO);
  const pontoTeste = { lat: CENTRO.lat + RAIO / 111320, lng: CENTRO.lng };
  const d = calcularDistancia(CENTRO.lat, CENTRO.lng, pontoTeste.lat, pontoTeste.lng);
  verdadeiro(d <= RAIO + 1, 'ponto na borda do raio');
  verdadeiro(pontoTeste.lat <= b.norte + 1e-9, 'o ponto da borda norte cabe no bounds');
});

teste('raio maior produz área maior no mapa', () => {
  const b1 = calcularBoundsEmVolta(CENTRO, 1000);
  const b2 = calcularBoundsEmVolta(CENTRO, 2000);
  verdadeiro((b2.norte - b2.sul) > (b1.norte - b1.sul));
});

teste('span leste-oeste acompanha a latitude (escala plana)', () => {
  const b = calcularBoundsEmVolta(CENTRO, RAIO);
  const spanLng = (b.leste - b.oeste) * 111320 * Math.cos(CENTRO.lat * Math.PI / 180);
  proximo(2 * RAIO, spanLng, 200);
});
